#include <IRremote.h>

int triggerPIN = 12;  //Pin odpowiedzialny za spust
int bluetoothConfigurationPINOutput = 8;  //Blokada edycji ustawien modulu bluetooth oraz ustawien broni (przelacznik)
int bluetoothConfigurationPINInput = 7;   //jw.
int irReceiverPIN = 4;  //Pin na ktory trafiaja dane z odbiornika IR
int isAlivePIN = 2;

int incapacitatedLock = 0;    //Blokada (martwy/brak amunicji)
int triggerLock = 0;          //Blokad (po kazdym strzale sie wlacza)

char command[30];              //komendy przesylane przez BT
short int weaponSignalCode=0;  //kod broni

IRsend irsend;
IRrecv irrecv(irReceiverPIN);
decode_results results;

boolean cmpCommand(char *cmdA, char *cmdB)  //funckja porownojaca komendy
{
  for(int i=0; i<3; i++)
  {
    if(cmdA[i]!=cmdB[i])
    {
      return false;
    }
  }
  return true;
}
void setPincode(char *pinCode)  //funkcja zmienia PIN na bluetooth
{
  char sendCMD[10];
  String cmd="AT+PIN";
  for(int i=0; i<4; i++)
  {
    cmd+=pinCode[i]; 
  }
  cmd.toCharArray(sendCMD, 10);
  Serial1.write(sendCMD,10);
}

void setName(char *newName, int length)  //funkcja zmienia nazwe bluetooth
{
  char sendCMD[27];
  String cmd="AT+NAME";
  for(int i=0; i<length; i++)
  {
    cmd+=newName[i]; 
  }
  cmd+="/g";
  cmd.toCharArray(sendCMD, 9+length);
  Serial1.write(sendCMD, 9+length);
}
void setup()
{
  //ustalenie parametrow polaczenia BT i USB
  Serial.begin(115200);
  Serial1.begin(9600);
  Serial.setTimeout(1000);
  Serial1.setTimeout(1000);
  
  //ustawienie trybow pinow
  pinMode(triggerPIN, INPUT);
  pinMode(irReceiverPIN, INPUT);
  pinMode(bluetoothConfigurationPINInput, INPUT);
  pinMode(bluetoothConfigurationPINOutput, OUTPUT);
  pinMode(isAlivePIN, OUTPUT);
  
  //ustalenie stanow poczatkowych pinow
  digitalWrite(bluetoothConfigurationPINOutput, HIGH);
  digitalWrite(isAlivePIN, HIGH);
  
  //inne ustawienia
  irrecv.enableIRIn(); //wlaczenie receivera
}

void loop() 
{
  if(digitalRead(bluetoothConfigurationPINInput)==HIGH)    //fragment aktywny kiedy blokada fizyczna wlaczona
  {
    if(cmpCommand(command, "CWC"))  //pobranie nowego sygnalu broni
    {
      char cmd[5];
      delay(1000);
      Serial1.readBytes(cmd,5);
      weaponSignalCode = 100 * (cmd[0]-48) + 10 * (cmd[1]-48) + 1 * (cmd[2]-48);
      Serial1.write("OK",2);
    }
    else if(cmpCommand(command, "PIN"))  //pobranie i ustawienie nowego pinu dla BT
    {
      char pin[6];
      delay(1000);
      Serial1.readBytes(pin,6);
      setPincode(pin);
    }
    else if(cmpCommand(command, "NME"))  //pobranie i ustawienie nowej nazwy dla BT
    {
      char num[4];
      char name[20];
      delay(1000);
      Serial1.readBytes(num,4);
      Serial1.readBytes(name,(num[0]-48)*10+(num[1]-48)+2);
      setName(name, (num[0]-48)*10+(num[1]-48));
    }
    else if(cmpCommand(command, "WLF"))  //odblokowanie broni po smierci
    {
      incapacitatedLock=0;
      digitalWrite(isAlivePIN, HIGH);
    }
  }
  else
  {
    if(cmpCommand(command, "WLN"))        //zablokowanie broni w wypadku smierci
    {
      incapacitatedLock=1;
      digitalWrite(isAlivePIN, LOW);
    }
    else if(cmpCommand(command, "WLF"))    //odblokowanie broni po smierci
    {
      incapacitatedLock=0;
      digitalWrite(isAlivePIN, HIGH);
    }
    
    if (irrecv.decode(&results))          //odbieranie strzalow
    {
      if(results.value>=0&&results.value<=255)
      {
        char hitcode[6];
        hitcode[0]='S';
        hitcode[1]='H';
        hitcode[2]='T';
        int i = results.value;
        hitcode[5]=48+i%10;
        i=i/10;
        hitcode[4]=48+i%10;
        i=i/10;
        hitcode[3]=48+i;
        Serial1.write(hitcode, 6);
      }
      irrecv.resume(); // Receive the next value
    }
    
    if(digitalRead(triggerPIN)==HIGH && incapacitatedLock == 0 && triggerLock == 0)  //strzelanie (w trakcie strzalu zakladana jest blokada na spust => po kazdym strzale trzeba puscic spust)
    {
      triggerLock=1;
      irsend.sendRC6(weaponSignalCode, 8);
      irrecv.enableIRIn(); // Start the receiver
    }
    
    if(digitalRead(triggerPIN)==LOW && triggerLock == 1)        //sprawdzanie czy spust zostal puszczony
    {
      triggerLock=0;
    }
  }
  command[0]=0;
  command[1]=0;
  command[2]=0;
  command[3]=0;
  command[4]=0;
}
