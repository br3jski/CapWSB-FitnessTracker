Spring - egzamin. Z tego co obecnie powiedział: 
1. Czym rózni się `RequestBody` od `ResponseBody` ? 
`RequestBody` - mapowanie danych z żądania HTTP do obiektu, stosujemy w metodach kontrolerów, które przyjmuja dane wejściowe (np. z poziomu REST API). 
`ResponseBody` - mapuje obiekty Java na odpowiedź HTTP, stosowane w metodach fkontrolerów które zwracają dane do klienta


2. Jak zaplanować zadanie za pomocą `@Scheduled`? : opisowo!
- Włączyć obsługę planowania zadań dodając adnotację `@EnableScheduling`
- Utworzyć metodę, która będzie wykonywana zgodnie z harmonogramem i oznaczyć adnotacją `@Scheduled`, mozna uzyć parametrów `fixedRate`, `fixedDelay` lub np. `cron`
<br> <br> Parametry `@Scheduled`
`fixedRate`: Określa odstęp czasu (w milisekundach) między rozpoczęciami kolejnych wykonania zadania.
`fixedDelay`: Określa odstęp czasu (w milisekundach) między zakończeniem jednego wykonania a rozpoczęciem następnego.
`initialDelay`: Określa czas (w milisekundach) opóźnienia przed pierwszym wykonaniem zadania.
`cron`: Określa wyrażenie cron, które definiuje harmonogram wykonania zadania

3. W jaki sposób mozna monitorować aplikację ?
Za pomocą Actuatora oraz konkretnych metryk. Jakie metryki - podać dwie.
Dodać w pom.xml:  <br> <br>
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
Jak takie metryki włączyć W `application.yml`, np. `include health, metrics` 

4. Popatrzeć na Beany - jak stworzyć Beana?
**Adnotacja `@Component`**
Najprostszym sposobem jest oznaczenie klasy adnotacją `@Component`. Spring automatycznie wykryje tę klasę i zarejestruje ją jako bean.

```java
import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    // Implementacja klasy
}
```

**Druga metoda: Adnotacje specjalizowane: `@Service`, `@Repository`, `@Controller`**
Spring dostarcza również bardziej specjalizowane adnotacje, które są pochodnymi `@Component` i służą do oznaczania klas o specyficznych rolach.

```java
import org.springframework.stereotype.Service;

@Service
public class MyService {
    // Implementacja klasy
}
```

```java
import org.springframework.stereotype.Repository;

@Repository
public class MyRepository {
    // Implementacja klasy
}
```

```java
import org.springframework.stereotype.Controller;

@Controller
public class MyController {
    // Implementacja klasy
}
```



**6. Czym są adnotacje? **
Adnotacje w Javie to specjalne znaczniki, które można dodać do kodu (klas, metod, pól, parametrów), aby dostarczyć dodatkowych informacji. W Spring Boot adnotacje są szeroko używane do konfiguracji i zarządzania komponentami aplikacji. Dzięki nim można np. oznaczyć klasę jako komponent Springa (`@Component`), wskazać metodę jako punkt wejścia aplikacji (`@SpringBootApplication`), czy wstrzyknąć zależności (`@Autowired`). Adnotacje upraszczają konfigurację i zwiększają czytelność kodu.

**7. Czym się rózni serwis od komponentu**
W Spring Framework zarówno `@Service`, jak i `@Component` są adnotacjami używanymi do definiowania beanów, ale mają różne cele i konteksty użycia:
**Podsumowanie**
- **`@Component`**: Ogólna adnotacja do definiowania beanów. Może być używana wszędzie tam, gdzie chcemy oznaczyć klasę jako komponent Springa.
- **`@Service`**: Specjalizowana adnotacja do definiowania beanów, które zawierają logikę biznesową. Użycie tej adnotacji dodaje semantyczne znaczenie, że klasa pełni rolę serwisu.

W praktyce, wybór między `@Component` a `@Service` zależy od kontekstu i roli, jaką dana klasa pełni w aplikacji. Używanie specjalizowanych adnotacji (`@Service`, `@Repository`, `@Controller`) może zwiększyć czytelność i zrozumiałość kodu.


8. Jakie są zalety korzystania ze Springa? Co nam to daje?
   Korzystanie ze Spring Framework oferuje wiele zalet, w tym:

1. **Inwersja Kontroli (IoC)**: Ułatwia zarządzanie zależnościami i cyklem życia obiektów.
2. **Modularność**: Możliwość korzystania tylko z potrzebnych modułów (np. Spring Boot, Spring Data).
3. **Testowalność**: Łatwiejsze testowanie aplikacji dzięki wsparciu dla mockowania i wstrzykiwania zależności.
4. **Konfiguracja**: Obsługa konfiguracji zarówno w plikach XML, jak i za pomocą adnotacji oraz plików properties/yaml.
5. **Integracja**: Łatwe integrowanie z różnymi technologiami i frameworkami (Hibernate, JPA, JMS, etc.).
6. **Bezpieczeństwo**: Wbudowane mechanizmy zabezpieczeń (Spring Security).
7. **Wsparcie dla transakcji**: Zarządzanie transakcjami w sposób deklaratywny.
8. **Społeczność i dokumentacja**: Duża społeczność użytkowników i bogata dokumentacja.

9. Jak wygląda podstawowa architektura aplikacji w Springu?
**Warstwa Prezentacji (Web Layer)**:
   - **Kontrolery**: Klasy oznaczone adnotacją `@Controller` lub `@RestController`, które obsługują żądania HTTP i zwracają odpowiedzi.

**Warstwa Serwisowa (Service Layer)**:
   - **Serwisy**: Klasy oznaczone adnotacją `@Service`, które zawierają logikę biznesową aplikacji.

**Warstwa Dostępu do Danych (Data Access Layer)**:
   - **Repozytoria**: Klasy oznaczone adnotacją `@Repository`, które zarządzają operacjami CRUD na bazie danych.

**Warstwa Modelu (Model Layer)**:
   - **Encje**: Klasy reprezentujące dane aplikacji, często oznaczone adnotacjami JPA (`@Entity`).
