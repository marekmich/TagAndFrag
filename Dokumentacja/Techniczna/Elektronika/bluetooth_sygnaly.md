Sygna�y nadawane i odbierane przez elektronik�
==============================================

*Autor: �ukasz �urawski*  
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespo�owe*  
*WMiI, UMK rok 2014/15*  
*Zesp� nr 14.*  


Sygna�y odbierane
-----------------

**Uwaga**  
Wszystkie elementy sygna�u wys�anego powinny ko�czy� znakiem CRLF (\r\n)


### Ustawianie nowego kodu broni

Sygna� sk�ada si� z dw�ch element�w.  
Pierwszym elementem sygna�u jest polecenie uruchamiaj�ce procedur� zmiany kodu broni:  
`CWC\r\n`  
Drugim elementem jest nowy kod broni. Musi to by� liczba w przedziale 0-255.   
`kod\r\n` 

W wypadku udanej zmiany kodu broni nadawany jest:  
`OK`  

**Uwaga**  
Kod powinien by� wys�any przed up�ywem jednej sekundy od nadania polecenia.  
Kod powinien by� wys�any w postaci trzech cyfr.  
Poprawno�� nowego kodu powinna by� weryfikowana po stronie nadawcy.  


### Ustawianie nowego pinu w module bluetooth

Sygna� sk�ada si� z dw�ch element�w.  
Pierwszym elementem sygna�u jest polecenie uruchamiaj�ce procedur� zmiany pinu.  
`PIN\r\n`  
Drugim elementem jest nowy pin.  
`npin\r\n`  


W wypadku udanej zmiany pinu nadawany jest:  
`AT+PINnpin`  
`OK`  

**Uwaga**  
Nowy pin powinien by� wys�any przed up�ywem jednej sekundy od nadania polecenia.  
Nowy pin powinien by� wys�any w postaci czterech cyfr.  
Poprawno�� nowego pinu powinna by� weryfikowana po stronie nadawcy.  


### Ustawianie nowej nazwy w module bluetooth

Sygna� sk�ada si� z dw�ch element�w.  
Pierwszym elementem sygna�u jest polecenie uruchamiaj�ce procedur� zmiany nazwy.  
`PIN\r\n`  
Drugim elementem jest nowa nazwa dla modu�u bluetooth.  
`name\r\n`  

W wypadku udanej zmiany nazwy nadawany jest:  
`AT+NAMEname/g`  
`OK`  

**Uwaga**  
Nowy pin powinien by� wys�any przed up�ywem jednej sekundy od nadania polecenia.  
Nowy pin powinien by� wys�any w postaci czterech cyfr.  
Poprawno�� nowego pinu powinna by� weryfikowana po stronie nadawcy.  