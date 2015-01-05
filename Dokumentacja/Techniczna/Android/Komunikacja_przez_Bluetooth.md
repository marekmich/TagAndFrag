##Komunikacja z układem przez Bluetooth

*Autor: Mateusz Wrzos* <br />
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespołowe* <br />
*UMK rok 2014/15* <br />
*Zespół nr 14.* <br />

###0. Wstęp
Aby nasza aplikacja mogła korzystać z adaptera Bluetooth wbudowanego w urządzenie, należy do pliku *AndroidManifest.xml* dodać uprawnienia:
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

###1. Włączanie modułu Bluetooth
Wystarczy utworzyć obiekt typu *BluetoothAdapter*, wywołać statyczną metodę *getDefaultAdapter()* która to zwróci domyślny adapter Bluetooth w naszym urządzeniu:
```java
BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
```
Bluetooth w naszej aplikacji jest konieczny do funkcjonowania systemu, więc na starcie możemy sprawdzić czy moduł jest włączony. Jeżeli nie, możemy stworzyć *Intent* włączający moduł:
```java
if (!bluetoothAdapter.isEnabled()) {
    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
}
```
###2. Łączenie się z układem
Po uprzednim sparowaniu urządzeń, wywołujemy metodę *getBondedDevices* na naszym adapterze:
```java
Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
```
Zwraca ona zbiór sparowanych urządzeń, z którego odzyskujemy nasz układ, a konkretnie jego adres MAC i zapisujemy do zmiennej typu *String*. <br />
Tworzymy obiekt *BluetoothDevice* reprezentujący odbiornik zamontowany w układzie naszej broni, inicjalizujemy go za pomocą metody `getRemoteDevice(String address)` przyjmującej uprzednio odzyskany adres MAC zapisany w zmiennej *address*, anulując jednocześnie aktualny proces wyszukiwania urządzeń:
```java
BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
bluetoothAdapter.cancelDiscovery();
```

###3. Odbiór danych
Aby jednoznacznie zidentyfikować naszą usługę Bluetooth konieczne będzie posiadanie UUID. Jest to 128-bitowa wartość, na tyle duża aby mogła być jednoznaczna. Można użyć jednego z generatorów dostępnego w sieci, na przykład:
```java
private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
```
Teraz, musimy zainicjalizować gniazdo i się połączyć:
```java
BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
```
Mając gniazdo, możemy zacząć nasłuchiwać przychodzących wiadomości. Do tego celu niezbędna jest klasa *InputStream* oraz bardzo przydatna *Scanner*:
```java
InputStream inputStream = bluetoothSocket.getInputStream();
Scanner inputScanner = new Scanner(inputStream);
```
Po inicjalizacji strumienia wejściowego oraz skanera, możemy zacząć nasłuchiwać przychodzących wiadomości, oczywiście w nowym wątku:
```java
while(inputScanner.hasNextLine()) {
  String message = inputScanner.nextLine();
  /*
    W tym miejscu operujemy na otrzymanej wiadomości, 
    np. przesyłamy ją do innej aktywności za pomocą Handler'a
  */
}
```
