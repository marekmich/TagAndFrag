Sygna�y nadawane i odbierane przez elektronik�
==============================================

Autor: �ukasz �urawski
W ramach projektu Tag & Frag na przedmiot Programowanie Zespo�owe 
WMiI, UMK rok 2014/15 
Zesp� nr 14. 


Sygna�y odbierane
-----------------

**Uwaga**
Wszystkie elementy sygna�u wys�anego powinny ko�czy� znakiem CRLF (\r\n)

### Ustawianie nowego kodu broni

Sygna� sk�ada si� z dw�ch element�w.
Pierwszym elementem sygna�u jest polecenie uruchamiaj�ce procedur� zmiany kodu broni:
`CWC\r\n`
Drugim elementem jest nowy kod broni. Musi to by� liczba w przedziale 0-255, dodatkowo wa�ne jest by liczba by�a zawsze wysy�ana w postaci trzech cyfr.
`kod\r\n` 


