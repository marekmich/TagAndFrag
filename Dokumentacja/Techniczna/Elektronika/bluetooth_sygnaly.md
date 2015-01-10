Sygna�y nadawane i odbierane przez elektronik�
==============================================

*Autor: �ukasz �urawski*  
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespo�owe*  
*WMiI, UMK rok 2014/15*  
*Zesp� nr 14.*  


Sygna�y odbierane
-----------------

**Uwaga**  
Wszystkie elementy sygna�u odbieranego przez elektronik� powinny ko�czy� znakiem CRLF (\r\n)  


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
Drugim elementem nowa nazwa dla modu�u bluetooth.  
`DLname\r\n`  

W wypadku udanej zmiany nazwy nadawany jest:  
`AT+NAMEname/g`  
`OK`  

**Uwaga**  
Nowa nazwa powinna by� wys�ana przed up�ywem jednej sekundy od nadania polecenia.  
Nowy nazwa powinna by� wys�ana w postaci maksymalnie 18 znakowego kodu, gdzie pierwsze znaki m�wi� o d�ugo�ci nowej nazwy, a w pozosta�ych 16 znajduje si� nowa nazwa dla modu�u bluetooth.  


### Odblokowanie broni

Sygna� sk�ada si� z jednego elementu.  
`WLF\r\n`  

W wypadku, gdy bro� jest zablokowana (dioda led zgaszona), a zostanie wys�any sygna� odblokowuj�cy, to dioda led zostanie zapalona, a strzelanie z broni zostanie odblokowane.  
W wypadku, gdy bro� jest oblokowana (dioda led zapalona), a zostanie wys�any sygna� odblokowuj�cy, to nic si� nie zdarzy.  


### Blokowanie broni

Sygna� sk�ada si� z jednego elementu.  
`WLN\r\n`  

W wypadku, gdy bro� jest zablokowana (dioda led zgaszona), a zostanie wys�any sygna� blokuj�cy, to nic si� nie zdarzy.  
W wypadku, gdy bro� jest oblokowana (dioda led zapalona), a zostanie wys�any sygna� blokuj�cy, to dioda led zostanie zgaszona, a strzelanie z broni zostanie zablokowane.  



Sygna�y nadawane
----------------

**Uwaga**  
W odr�nieniu od sygna��w odbieranych, sygna�y wysy�ane przez elektronik� nie ko�cz� si� znakiem CRLF (\r\n)  


### Trafienie przez innego gracza

Sygna� sk�ada si� z jednego elementu o d�ugo�ci sze�ciu znak�w.  
`SHTkod`  

Pierwsze trzy znaki informuj� o tym, �e nadawca sygna�u zosta� trafiony
Pozosta�e trzy znaki informuj� o kodzie broni, kt�ra trafi�a nadawc� sygna�u