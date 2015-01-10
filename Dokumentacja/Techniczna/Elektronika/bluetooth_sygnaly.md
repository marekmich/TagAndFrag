Sygna³y nadawane i odbierane przez elektronikê
==============================================

*Autor: £ukasz ¯urawski*  
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespo³owe*  
*WMiI, UMK rok 2014/15*  
*Zespó³ nr 14.*  


Sygna³y odbierane
-----------------

**Uwaga**  
Wszystkie elementy sygna³u odbieranego przez elektronikê powinny koñczyæ znakiem CRLF (\r\n)  


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
Drugim elementem nowa nazwa dla modu³u bluetooth.  
`DLname\r\n`  

W wypadku udanej zmiany nazwy nadawany jest:  
`AT+NAMEname/g`  
`OK`  

**Uwaga**  
Nowa nazwa powinna byæ wys³ana przed up³ywem jednej sekundy od nadania polecenia.  
Nowy nazwa powinna byæ wys³ana w postaci maksymalnie 18 znakowego kodu, gdzie pierwsze znaki mówi¹ o d³ugoœci nowej nazwy, a w pozosta³ych 16 znajduje siê nowa nazwa dla modu³u bluetooth.  


### Odblokowanie broni

Sygna³ sk³ada siê z jednego elementu.  
`WLF\r\n`  

W wypadku, gdy broñ jest zablokowana (dioda led zgaszona), a zostanie wys³any sygna³ odblokowuj¹cy, to dioda led zostanie zapalona, a strzelanie z broni zostanie odblokowane.  
W wypadku, gdy broñ jest oblokowana (dioda led zapalona), a zostanie wys³any sygna³ odblokowuj¹cy, to nic siê nie zdarzy.  


### Blokowanie broni

Sygna³ sk³ada siê z jednego elementu.  
`WLN\r\n`  

W wypadku, gdy broñ jest zablokowana (dioda led zgaszona), a zostanie wys³any sygna³ blokuj¹cy, to nic siê nie zdarzy.  
W wypadku, gdy broñ jest oblokowana (dioda led zapalona), a zostanie wys³any sygna³ blokuj¹cy, to dioda led zostanie zgaszona, a strzelanie z broni zostanie zablokowane.  



Sygna³y nadawane
----------------

**Uwaga**  
W odró¿nieniu od sygna³ów odbieranych, sygna³y wysy³ane przez elektronikê nie koñcz¹ siê znakiem CRLF (\r\n)  


### Trafienie przez innego gracza

Sygna³ sk³ada siê z jednego elementu o d³ugoœci szeœciu znaków.  
`SHTkod`  

Pierwsze trzy znaki informuj¹ o tym, ¿e nadawca sygna³u zosta³ trafiony
Pozosta³e trzy znaki informuj¹ o kodzie broni, która trafi³a nadawcê sygna³u