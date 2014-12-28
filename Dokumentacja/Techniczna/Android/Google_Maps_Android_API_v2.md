##Google Maps Android API v2

*Autor: Mateusz Wrzos* <br />
*W ramach projektu Tag & Frag na przedmiot Programowanie Zespołowe* <br />
*WMiI, UMK rok 2014/15* <br />
*Zespół nr 14.* <br />

##0. Wstęp
Zanim rozpoczniemy pracę z Google Maps API v2 pamiętajmy aby do naszego projektu dołączyć bibliotekę:
```xml
google-play-services_lib
```
pobraną za pomocą Android SDK Manager'a.

Kolejną niezbędną rzeczą jest klucz do Google Maps API wygenerowany za pomocą Google APIs Console.
Następnie dodajemy go do pliku *AndroidManifest.xml* w znaczniku *<application>* w ten sposób:
```xml
<meta-data
	android:name="com.google.android.maps.v2.API_KEY"
	android:value="WYGENEROWANY_KLUCZ"/>
```
Pamiętaj że klucz jest przypisany do projektu (identyfikowany przez podany package podczas generowania).

Następnie, dodajemy uprawnienia do pliku AndroidManifest.xml w znaczniku *<manifest>*:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

###1. Dodawanie mapy <br />
Wystarczy że do naszej aktywności na której ma być wyświetlona mapa dodamy fragment:
```xml
<fragment 
	android:id="@+id/map"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:name="com.google.android.gms.maps.MapFragment"/>
```
W tym przypadku doda się fragment z mapą o id *map* oraz wypełni całą aktywność.

###2. Działania na mapie <br />
Do naszej aktywności wyświetlającej mapę dodajemy pole:
```java
private GoogleMap googleMap;
```
W metodzie *onCreate* musimy zainicjalizować naszą mapę (dodany fragment) za pomocą id. Nasza mapa ma id *map*, więc:
```java
googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
```
Teraz możemy zacząc pracować z naszą mapą. <br />
Aby dodać warstwę która nas lokalizuje (guzik po którego wciśnięciu przeniesie nas do naszej lokalizacji oraz guziki + oraz -) wystarczy:
```java
googleMap.setMyLocationEnabled(true);
```
###3. Interakcja, dodawanie znaczników
Aby wykryć klikniecia na mapie, nasza aktywność musi implementować interfejs *OnMapClickListener* z metodą:
```java
public void onMapClick(LatLng point) {
}
```
Aby dodać znacznik przyda nam się klasa *MarkerOptions*, pozwalająca ustawić konkretne opcje na danym znaczniku. <br />
Do zmiany ikony danego znacznika, przyda się nam klasa *BitmapDescriptor* jak i rownież fabryka *BitmapDescriptorFactory*. <br />
Weźmy jakąś przykładową, gotową ikonę z *drawable*, niech to będzie *ic_corp_icon*:
```java
BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_corp_icon);
```
Na wcześniej utworzonym obiekcie *MarkerOptions* ustawiamy pozycję *point* (argument metody *onMapClick*), ikonę z fabryki *BitmapDescriptorFactory*, oraz np. tytuł i dodajemy znacznik na naszą mapę (wcześniej zainicjalizowaną w metodzie *onCreate*):
```java
public void onMapClick(LatLng point) {
	BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_corp_icon);
	MarkerOptions options = new MarkerOptions();
	options.position(point).icon(icon).title("Apteczka");
	googleMap.addMarker(options);
}
```
