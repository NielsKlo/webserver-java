In deze opdracht gaan we onze eigen webserver opzetten. Jullie hebben al ervaring met het opzetten van een programma dat input van gebruikers afhandelt. Een webserver is qua concept niet heel veel anders. In plaats van dat de client direct tegen de applicatie praat, verloopt de communicatie nu via een internetlijntje - een socket. We gaan stapsgewijs een eigen framework bouwen middels de basisgereedschappen (low level primitives) van de taal.

## Ingrediënt 1: listening loop

We gaan hier de algemene concepten van een webserver uitleggen. We hebben het een en ander voor je opgezet in deze repository. De code is ook uitgebreid gedocumenteerd, zodat het hopelijk zichzelf uitwijst. Grofweg bestaat de opzet uit:

```bash
1. Zet een poort open over de TCP-communicatie
2. Herhaal ad infinitum:
     a. Ontvang verbinding op een zogenaamde socket
     b. Verwerk bericht op de achtergrond
```

Dit stappenplan kun je vinden in de `main`-methode. Hier start je applicatie en tref je de voorbereidingen om requests te ontvangen over het internet. De applicatie begint te luisteren naar een inkomend verzoek op poort 9090. Een binnenkomend verzoek wordt toegewezen aan een socket (als het ware een soort telefoonlijn), waarover je heen en weer communicatie kan uitwisselen. In principe is binnen je applicatie hiermee alles geregeld om te kunnen communiceren met de buitenwereld. Wat andere computers belet om met jouw pc te verbinden, is over het algemeen de firewall-instellingen van je OS. Deze staan standaard beperkt ingesteld om de kans op aanvallen te verkleinen.

## Ingrediënt 2: Basiscommunicatie

Als we in wat meer detail gaan kijken naar de afhandeling van het bericht, dan komen we op het volgende stappenplan:

```bash
1. Lees het inkomende bericht uit. Herhaal tot we een lege regel tegenkomen:
     a. Lees een regel uit de socket.
     b. Schrijf de regel naar de standard output.
2. Verzend een eenvoudig bericht naar de client over de socket.
```

Door dit te implementeren (refereer naar de gegeven code om te zien wat de details van de implementatie zijn) hebben we de communicatie heen en weer in hele simpele vorm bewerkstelligd. We kunnen in onze output de binnengekomen informatie zien en we sturen een bericht terug naar de client. Je kunt de webserver opstarten en kijken of we de waarheid spreken. De webserver is benaderbaar op localhost:9090. Zoals we straks zullen zien, voldoet dit bericht geenszins aan de [HTTP-standaard](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol). Dit betekent dat de gemiddelde browser het antwoord van de webserver niet zal accepteren en een foutpagina zal tonen. Je kunt gebruik maken van [Postman](https://www.getpostman.com/) of [curl](https://curl.haxx.se/) om het bericht te tonen.

## Opdracht

De overkoepelende opdracht is om de webserver uit te breiden zodat het aan de HTTP-standaard voldoet. Hierbij willen we voor onszelf een laag over de primitives heen bouwen, wat ons op een hoger niveau in staat stelt om de HTTP-definities te herkennen. We gaan een klasse bouwen dat het binnenkomende bericht uitleest en omvormt tot een netta HTTP request. Evenzo willen wij ons antwoord op een high level kunnen definiëren als een HTTP response, die we vervolgens volgens de standaard weg kunnen schrijven over de verbinding. Gedurende de opdracht brengen we interactieve elementen in de webserver, zodat het antwoord verandert aan de hand van de binnengekomen informatie.

### Opdracht 1: HTTP request

Voor een volledig functionele webserver is het noodzakelijk om het binnenkomende requestbericht correct te interpreteren. Aan de hand hiervan kan besloten worden welke logica uitgevoerd wordt. Dit principe heet routing; het bepalen welke methode bij een bepaald pad hoort. Een [requestbericht volgens het HTTP-protocol](https://tools.ietf.org/html/rfc7230#section-3) heeft de volgende structuur:

```xml
<http methode> <resource path> <http versie>
<header parameter1>: <header parameter1 waarde>
<header parameter2>: <header parameter2 waarde>
…
<header parameterN>: <header parameterN waarde>
[lege regel]
<body regel1>
…
<body regelN>
```

De eerste regel, bestaande uit de HTTP-methode, opgevraagde resource en de HTTP-versie wordt gebruikt om eerdergenoemde logica uit te voeren. Voor deze opdracht zullen we de simpele basis van HTTP/1.1 aanhouden, waarbij de browser de vraag stelt en de server antwoord geeft. [HTTP/2](https://daniel.haxx.se/http2/) biedt de mogelijkheid om de verbinding in stand te houden, waardoor de server actief informatie kan pushen (voor bijvoorbeeld liveblogs, chatrooms of om de hoeveelheid requests voor de doorsnee webpagina te reduceren). HTTP/3 is op tijde van schrijven in de prototype-fase.

Het eerste blok aan regels zijn de zogenaamde headers. Dit is de metadata die een client (of webserver, zoals we later gaan zien bij het opbouwen van de response) meestuurt met het request. Het internet, en met name HTTP/1.1, is stateless; dat betekent dat een webserver in principe geen idee heeft wie er aan de andere kant van de lijn zit en of deze persoon al meerdere requests heeft gedaan. Een manier om de continuïteit te bewaren zijn de befaamde cookies. Deze worden ook als een header meegegeven. Verder kan de client aangeven wat de technische limitaties en gebruikerspreferenties zijn.

Na de headers volgt altijd een witregel. Dit is de manier waarop de server weet dat alle headers overgekomen zijn. Indien aanwezig volgt daarna in de body van het requestbericht (alleen aanwezig bij PUT/POST, bijvoorbeeld als je een formulier opstuurt). De structuur van deze body is vrij en wordt gecommuniceerd via de metadata.

Als je verder in de details duikt, zul je zien dat er in de HTTP-methode, het resource path en de HTTP-versie geen spaties mogen zitten. De line-endings zijn Windows, dus `\r\n`.

Een concreet voorbeeld:

```
GET /books HTTP/1.1
Host: localhost:9090
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: nl,en-us;q=0.7,en;q=0.3
Accept-Encoding: gzip,deflate
Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
Keep-Alive: 300
Connection: keep-alive

```

In de repository zit een request interface. Deze interface bevat high-level methodes om de boven beschreven informatie uit het request op te vragen. Schrijf een class die dit interface implementeert (`class Foo implements Request`), als je dat doet zul je zien dat je compiler errors krijgt. Het interface verplicht dat jouw class de zes methodes die erin gedefinieerd zijn implementeert. Schrijf voor de eerste vier een implementatie, de voor de twee niet-Header varianten kun je voorlopig een dummy implementatie schrijven; dit komt bij opdracht 3. Bedenk voor de vier methodes zelf hoe de informatie het beste tot die klasse kan komen.
Als je de request hebt uitgelezen, kun je de uitgaande response aanpassen zodat deze afhangt van het binnengekomen bericht, zoals bijvoorbeeld:  “You did an HTTP GET request and you requested the following resource: /books”.


### Opdracht 2: HTTP response

De volgende stap is om het antwoord van de server te formaliseren. Tot nu toe stuurden we een simpele tekstregel terug, waar menig browser vervolgens op afhaakte. We gaan in deze stap een formeel HTTP-antwoord opbouwen. Net als de request heeft ook de HTTP response een vaste structuur:

```xml
<http versie> <response status code> <korte beschrijving van de status code> 
<header parameter1>: <header parameter1 waarde>
<header parameter2>: <header parameter2 waarde>
…
<header parameterN>: <header parameterN waarde>
[lege regel]
<body regel1>
…
<body regelN>
```

De response-structuur volgt grotendeels de structuur van het request. De eerste regel vertelt hoe de server het verzoek heeft kunnen afhandelen (bijvoorbeeld 200 OK, maar er zijn [talloze status codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)). Voor onze opdracht staat de HTTP-versie vast op HTTP/1.1. Daarnaast staat er metadata in de headers, volgt er weer een witregel en eventueel een body.

Een voorbeeld van response van een Apache webserver:

```
HTTP/1.1 200 OK
Date: Tue, 21 Jul 2015 11:02:13 GMT
Server: Apache/2.4.16 (Unix) OpenSSL/1.0.2d PHP/5.4.45
Connection: close
Content-Type: text/html; charset=UTF-8
Content-Length: 90

<html>
<body>
You did an HTTP GET request.<br/>
Requested resource: /books
</body>
</html>
```

In dit voorbeeld wordt er een antwoord teruggestuurd met een body (wat in de regel het geval is). Dit houdt in dat de headers `Content-Type` en `Content-Length` (in bytes) meegeleverd en correct moeten zijn. `Connection: close` betekent dat de server na het bericht de verbinding verbreekt. `Date` geeft de datum in [RFC1123-formaat](https://www.ietf.org/rfc/rfc1123.txt). De `Server`-header mag in principe verzonnen worden; dit is puur een identificatie voor de host. In theorie weet de browser aan de hand van de server of bepaalde functionaliteit wel of niet ondersteund wordt (dit wordt omgekeerd wel gebruikt, dat er andere pagina's geleverd worden aan gebruikers van Internet Explorer).

In de repository staat een interface voor de response, die de elementen van het antwoord definieert. Maak een implementatie voor deze interface. Zorg ook dat, gegeven een object die de interface implementeert, de response wordt weggeschreven naar de uitgaande socket. Als alles goed gaat, dan zul je in de browser de body terugzien. Uitgaande van het voorbeeld, betekent dat de tekst "You did an HTTP GET request...". De meegestuurde headers zijn niet direct zichtbaar; de browser gebruikt dit alleen om het antwoord te interpreteren. Door in de developer tools van de browsers (F12 in Chrome en Firefox) te kijken, kun je alle informatie van de response bekijken.

### Opdracht 3: Request parameters

Tot nu toe hebben we de minimale interpretatie van een HTTP request geïmplementeerd, namelijk alleen het vertalen van het standaardformaat. Soms wil een browser extra informatie meegeven aan de server behorend bij het request. Denk hierbij aan een zoekopdracht in je favoriete zoekmachine. Dit is niet echt een onderdeel van de metadata, maar meer inharent aan het request zelf. Om deze informatie mee te kunnen sturen, zijn url parameters bedacht. Voor een HTTP GET request zitten de parameters in de url, ofwel de eerste regel van het request:

```
GET /search?hl=nl&q=java HTTP/1.1
…
```

In dit voorbeeld is het opgevraagde resource path nog steeds `/search` en zijn er twee parameters: `hl` en `q`, met de waarde `nl` en `java` respectievelijk. Formeel gezien kan de url als volgt uitgedrukt worden:

```
<resource path>?<parameter1 naam>=<parameter1 waarde>&…&<parameterN naam>=<parameterN waarde>
```

Bij een HTTP POST request zijn er meerdere mogelijke manieren om parameters mee te geven. De gebruikte manier wordt aangegeven in de `Content-Type` header. De meegestuurde waardes zijn te vinden in de body van het request. Naast dat er meer vrijheid is in de vorm van de data (van url parameter vorm tot binaire data), is er ook de mogelijkheid om dit met HTTPS (SSL) te versleutelen. De is buiten de scope van de opdracht, maar wel relevante informatie. Een volledig POST request ziet er als volgt uit:

```
POST /search HTTP/1.1
…
Content-Length: 12
Content-Type: application/x-www-form-urlencoded

hl=nl&q=java
```

Implementeer de (url) parameter methodes in de implementatie van de request interface voor zowel HTTP GET als HTTP POST. Pas het teruggegeven bericht aan zodat het de meegestuurde header en url parameters teruggeeft:

```
You did an http GET request and you requested the following resource: /books


The following header parameters where passed:
Host: localhost:9090
Connection: keep-alive
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/530.5 (KHTML, like Gecko) Chrome/2.0.172.37 Safari/530.5
Cache-Control: max-age=0
…

The following parameters were passed:
Titel: SCWCD Exam Study Kit – Java Web Component Developer Certification
Publisher: Manning

```

Je kunt de implementatie van POST testen met tools als bijvoorbeeld [Postman](https://www.getpostman.com/).

## Conclusie
Je hebt net een webserver gemaakt die een inkomend request (in byte stream vorm) uitleest en in een voor de ontwikkelaar begrijpbare klasse omzet. Ook heb je een functie geschreven die een klasse omzet in een uitgaande byte stream. Hierdoor heb je een zogenaamde high-level laag (minder primitief en beter begrijpbaar) om de sockets heen gemaakt. Dit principe noemt men een framework, iets wat andere ontwikkelaars als basis kunnen gebruiken om hun eigen webapplicatie op te bouwen. Het is in principe niet aan de webserver om de logica te bepalen, maar aan de webapplicatie. De webserver heeft de fundering gelegd om dit mogelijk te maken.

In plaats van relatieve hard-coded content teruggeven, zou je bestanden van de schijf kunnen halen (zoals HTML en Javascript bestanden) op basis van het opgevraagde resource path. Zo kan je met weinig moeite al een site opzetten. Deze zal nog relatief statisch zijn, in de zin van dat er geen achterliggend gedrag is waarmee je ingevulde gegevens kan persisteren. Met de Javascript bestanden zou je de door jou gebouwde webserver al kunnen gebruiken om een interactieve site te maken.
