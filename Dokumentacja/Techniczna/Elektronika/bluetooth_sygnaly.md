Sygna³y nadawane i odbierane przez elektronikê
==============================================

Autor: £ukasz ¯urawski
W ramach projektu Tag & Frag na przedmiot Programowanie Zespo³owe 
WMiI, UMK rok 2014/15 
Zespó³ nr 14. 


Sygna³y odbierane
-----------------

**Uwaga**
Wszystkie elementy sygna³u wys³anego powinny koñczyæ znakiem CRLF (\r\n)

### Ustawianie nowego kodu broni

Sygna³ sk³ada siê z dwóch elementów.
Pierwszym elementem sygna³u jest polecenie uruchamiaj¹ce procedurê zmiany kodu broni:
`CWC\r\n`
Drugim elementem jest nowy kod broni. Musi to byæ liczba w przedziale 0-255, dodatkowo wa¿ne jest by liczba by³a zawsze wysy³ana w postaci trzech cyfr.
`kod\r\n` 


