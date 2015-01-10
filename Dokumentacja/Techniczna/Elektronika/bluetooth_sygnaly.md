Sygna³y nadawane i odbierane przez elektronikê
==============================================

*Autor: £ukasz ¯urawski*  
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespo³owe*  
*WMiI, UMK rok 2014/15*  
*Zespó³ nr 14.*  


Sygna³y odbierane
-----------------

**Uwaga**  
Wszystkie elementy sygna³u wys³anego powinny koñczyæ znakiem CRLF (\r\n)


### Ustawianie nowego kodu broni

Sygna³ sk³ada siê z dwóch elementów.  
Pierwszym elementem sygna³u jest polecenie uruchamiaj¹ce procedurê zmiany kodu broni:  
`CWC\r\n`  
Drugim elementem jest nowy kod broni. Musi to byæ liczba w przedziale 0-255.   
`kod\r\n` 

W wypadku udanej zmiany kodu broni nadawany jest:  
`OK`  

**Uwaga**  
Kod powinien byæ wys³any przed up³ywem jednej sekundy od nadania polecenia.  
Kod powinien byæ wys³any w postaci trzech cyfr.  
Poprawnoœæ nowego kodu powinna byæ weryfikowana po stronie nadawcy.  


### Ustawianie nowego pinu w module bluetooth

Sygna³ sk³ada siê z dwóch elementów.  
Pierwszym elementem sygna³u jest polecenie uruchamiaj¹ce procedurê zmiany pinu.  
`PIN\r\n`  
Drugim elementem jest nowy pin.  
`npin\r\n`  


W wypadku udanej zmiany pinu nadawany jest:  
`AT+PINnpin`  
`OK`  

**Uwaga**  
Nowy pin powinien byæ wys³any przed up³ywem jednej sekundy od nadania polecenia.  
Nowy pin powinien byæ wys³any w postaci czterech cyfr.  
Poprawnoœæ nowego pinu powinna byæ weryfikowana po stronie nadawcy.  


### Ustawianie nowej nazwy w module bluetooth

Sygna³ sk³ada siê z dwóch elementów.  
Pierwszym elementem sygna³u jest polecenie uruchamiaj¹ce procedurê zmiany nazwy.  
`PIN\r\n`  
Drugim elementem jest nowa nazwa dla modu³u bluetooth.  
`name\r\n`  

W wypadku udanej zmiany nazwy nadawany jest:  
`AT+NAMEname/g`  
`OK`  

**Uwaga**  
Nowy pin powinien byæ wys³any przed up³ywem jednej sekundy od nadania polecenia.  
Nowy pin powinien byæ wys³any w postaci czterech cyfr.  
Poprawnoœæ nowego pinu powinna byæ weryfikowana po stronie nadawcy.  