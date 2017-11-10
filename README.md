# Stacja kosmiczna 
**Drugi projekt PRiR symulujący przesył towarów w stacji kosmicznej**

> W tym zadaniu sprawujecie Państwo kontrolę nad systemem przesyłania ładunków przez śluzy stacji księżycowej.
Państwa system ma odbierać od użytkowników zlecenia przesłania ładunków z i do stacji. Następnie, ładunki te bezpiecznie przekazywać za pomocą wielu śluz powietrznych.

## Działanie śluz

+ Śluza to mechanizm pozwalający na bezpieczną wymianę towarów z otoczeniem stacji przy minimalnej utracie powietrza.
+ Śluzy muszą być używane efektywnie - niedopuszczalna jest sytuacja, gdy istnieją ładunki, które mogą być przekazywane przez dostępne śluzy, a system tego nie robi.
+ Śluza może ulec zniszczeniu (błąd krytyczny systemu) tylko na skutek niefrasobliwego jej użycia. Sama z siebie się nie popsuje.
+ Śluzy działają niezależnie. Różnym śluzom można zlecać różne operacje w tym samym czasie.
+ Śluzy mają określony i stały rozmiar oraz dwoje drzwi: zewnętrzne i wewnętrzne.
+ Interfejs śluzy pozwala na zlecanie operacji, które mają być wykonane. Jest to działanie nieblokujące wątku użytkownika. Sama operacja może trwać pewien okres czasu i zostanie zakończona przekazaniem informacji o zmianie stanu śluzy do nasłuchującego na ten fakt obiektu (koniecznie trzeba go zarejestrować, bo inaczej nie będzie wiadomo kiedy śluza zakończyła realizację poprzedniej operacji). 
+ Operacje realizowane przez śluzę nigdy nie będą wykonywane natychmiastowo.
## Ładunek
+ Nigdy nie wolno doprowadzić do otwarcia obu drzwi jednocześnie.
+ Ładunek może zostać umieszczony w śluzie tylko wtedy, gdy będzie ona co najmniej takiej samej wielkości jak ładunek i odpowiednie drzwi zostaną wcześniej otwarte (np. w przypadku transferu ładunku do stacji muszą zostać otwarte drzwi zewnętrzne).
+ W śluzie może być umieszczony tylko jeden ładunek. Nawet jeśli pozostało wolne miejsce.
+ Zlecenie wykonania nowej operacji przed zakończeniem poprzedniej uznawane jest za błąd (np. w trakcie otwierania drzwi nie wolno nakazać ich zamknięcia).
+ Ładunek zostanie prawidłowo dostarczony jeśli otwarte zostaną właściwe drzwi w kierunku docelowym.
+ Jeśli ładunek jest już w śluzie i zlecona zostanie operacja jego wyprowadzenia, to zawsze poruszy się on w kierunku otwartych drzwi, nawet jeśli spowoduje to jego przemieszczenie w niewłaściwą stronę. 
> Np. ładunek powinien być dostarczony z zewnątrz do stacji. Znajduje się już w śluzie. Otwarte zostają drzwi zewnętrzne i zlecone zostaje wyprowadzenie ładunku ze śluzy - efekt: ładunek nie zostaje dostarczony do środka stacji lecz wraca na zewnątrz.
+ Ładunki o rozmiarze identycznym z rozmiarem śluzy mają w tej śluzie bezwzględne pierwszeństwo. 
> Np. załóżmy, że istnieje śluza o rozmiarze N i ładunki o rozmiarach mniejszych niż N. Śluza jest w użyciu. Jeśli metoda dodająca do systemu ładunek o rozmiarze N zostanie zakończona przed końcem obsługi otwierania drzwi śluzy, to wymaga się aby już następna operacja wykonywana przez tą śluzę dotyczyła obsługi dodanego właśnie ładunku - może nią być wprowadzenie do śluzy ładunku lub zamknięcie drzwi, jeśli ładunek wymaga przekazania w innym kierunku (np. śluza była przygotowywana do przekazania ładunku do wnętrza stacji, a dodany właśnie ładunek wymaga przesłania go z zewnątrz).
## Prawidłowy cykl przekazania ładunku przez śluzę to:
+ Otwarcie drzwi 1
+ Wprowadzenie ładunku do śluzy
+ Zamknięcie drzwi 1
+ Otwarcie drzwi 2
+ Wyprowadzenie ładunku ze śluzy
+ Zamknięcie drzwi 2.

**Uwaga**: w przypadku połączenia 2 kolejnych transferów ładunku przez śluzę można użyć cyklu uproszczonego i działać wg. schematu:

+ Otwarcie drzwi 1
+ Wprowadzenie ładunku 1 do śluzy
+ Zamknięcie drzwi 1
+ Otwarcie drzwi 2
+ Wyprowadzenie ładunku 1 ze śluzy
+ Wprowadzenie ładunku 2 do śluzy
+ Zamknięcie drzwi 2
Cykl ten nadaje się do obsługi par ładunków skierowanych w przeciwnych kierunkach.

## Wymagania systemu

+ Przyjmowanie zleceń ma być działaniem natychmiastowym - nie może blokować pracy użytkownika. 
+ System ma zapamiętać informację o zleceniu i możliwie szybko zakończyć metodę cargoTransfer.
+ System ma prawidłowo przyjmować zlecenia nawet w przypadku równoczesnego składania ich przez wielu użytkowników.
+ System musi działać, tzn. musi przekazywać przez dostępne śluzy ładunki. Jeśli uwzględnić sumaryczny czas trwania wszystkich operacji realizowanych przez śluzę i prowadzących do jednokrotnego przekazania ładunku (niech wynosi on T), to oczekuje się, że czas realizacji N zleceń przez K śluz o odpowiednim rozmiarze można będzie oszacować jako __T * ⌈ N / K ⌉__
+ Program nie może zajmować zasobów CPU w przypadku, gdy system nie wykonuje żadnych operacji (np. zlecenia są realizowane przez śluzy i program czeka na ich zakończenie). Oczekuje się, że utworzone przez system wątki (o ile takie będą) zostaną wstrzymane za pomocą metody wait() lub jej odpowiednika.
+ Zabronione jest wprowadzenie wątków w stan uśpienia (za pomocą metody sleep lub jej odpowiednika).

## Uwagi
+ Użytkownik nigdy nie dostarczy `null` do wywoływanych metod
+ Użytkownik gwarantuje jednokrotne wykonanie metody setAirlocksConfiguration przed zgłoszeniem pierwszego zlecenia
