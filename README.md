# EazyTG - Java Telegram Bot Framework

## Описание проекта
EazyTG - это простой и мощный Java фреймворк для создания Telegram ботов. Библиотека предоставляет удобный интерфейс для работы с Telegram Bot API, упрощая разработку ботов с богатым функционалом.

## Возможности
- Отправка текстовых сообщений
- Отправка фото, документов и медиа-групп
- Создание и управление клавиатурами (inline и reply)
- Редактирование сообщений и медиа-контента
- Обработка обратных вызовов (callback queries)
- Удобное логирование с контекстной информацией

## Установка

### Вариант 1: Использование через GitHub Packages

#### Gradle
Добавьте репозиторий GitHub Packages и зависимость в ваш build.gradle:

```gradle
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/ohchillman/eazy-telegram")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME_GITHUB")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN_GITHUB")
        }
    }
}

dependencies {
    implementation 'org.eazytg:eazy-telegram:1.1.6'
}
```

Для аутентификации в GitHub Packages добавьте в вашем окружении или в файле `gradle.properties` следующие переменные:
```properties
USERNAME_GITHUB=ваш_github_логин
TOKEN_GITHUB=ваш_github_токен
```

#### Maven
Добавьте репозиторий GitHub Packages и зависимость в ваш pom.xml:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/ohchillman/eazy-telegram</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.eazytg</groupId>
        <artifactId>eazy-telegram</artifactId>
        <version>1.1.6</version>
    </dependency>
</dependencies>
```

Для аутентификации создайте или отредактируйте файл `~/.m2/settings.xml`:
```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>ваш_github_логин</username>
      <password>ваш_github_токен</password>
    </server>
  </servers>
</settings>
```

### Вариант 2: Сборка из исходников

1. Клонировать репозиторий:
```bash
git clone https://github.com/your-username/eazytg.git
```

2. Создать файл `config.properties` в директории `src/main/resources` со следующим содержимым:
```properties
bot.username=ВашБотИмя
bot.token=ВашБотТокен
```

3. Соберите проект с помощью Gradle:
```bash
./gradlew build
```

или Maven:
```bash
mvn clean install
```

## Быстрый старт

### Создание и запуск бота
```java
import org.eazytg.ChatBot;
import org.eazytg.lib.Logs;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(chatBot);
            Logs.log("Бот успешно запущен.");
        } catch (TelegramApiException e) {
            Logs.error(e.getMessage());
        }
    }
}
```

### Обработка сообщений и отправка ответов
```java
private void messageHandler(String userText, long chatId, String userName) {
    // Создание кнопок
    List<Button> buttons = new ArrayList<>();
    buttons.add(new Button("Первая кнопка", "button1"));
    buttons.add(new Button("Вторая кнопка", "button2"));
    buttons.add(new Button("Ссылка", "https://telegram.org"));

// Опционально: кастомный эмодзи и стиль кнопки (danger/success/primary)
buttons.add(new Button("Опасное действие", "danger_action")
    .withIconCustomEmojiId("5440266917482170481")
    .withStyle(Button.Style.DANGER));
    
    // Создание клавиатуры (3 кнопки в ряд)
    InlineKeyboardMarkup keyboard = Telegram.createKeyboard(buttons, 3);
    
    // Отправка сообщения с клавиатурой
    Telegram.sendMessage(this, chatId, "Привет, " + userName + "! Вы написали: " + userText, keyboard, null);
}
```

### Обработка callback-запросов
```java
private void callbackHandler(Update update, String callbackData, long chatId) {
    switch (callbackData) {
        case "button1":
            Telegram.sendMessage(this, chatId, "Вы нажали на первую кнопку!", null, null);
            break;
        case "button2":
            Telegram.sendMessage(this, chatId, "Вы нажали на вторую кнопку!", null, null);
            break;
        default:
            Logs.log(chatId, "Неизвестный callback: " + callbackData);
    }
}
```

## Примеры использования

### Отправка сообщений с ответом (Reply)
```java
// Отправка сообщения с ответом на другое сообщение
int replyToMessageId = 12345; // ID сообщения, на которое нужно ответить
Telegram.sendMessage(this, chatId, "Это ответ на сообщение", null, replyToMessageId);

// Отправка фото с ответом на сообщение
Telegram.sendPhoto(this, chatId, "Фото в ответ", "https://example.com/photo.jpg", null, replyToMessageId);

// Отправка документа с ответом на сообщение
Telegram.sendDocument(this, chatId, "Документ в ответ", "https://example.com/document.pdf", null, replyToMessageId);
```

### Создание Reply-клавиатуры
```java
// Создание обычной клавиатуры (не inline)
List<String> buttonLabels = Arrays.asList(
    "Кнопка 1", "Кнопка 2", "Кнопка 3", 
    "Кнопка 4", "Кнопка 5", "Кнопка 6"
);
ReplyKeyboardMarkup replyKeyboard = Telegram.createReplyKeyboard(buttonLabels, 2);
Telegram.sendMessage(this, chatId, "Сообщение с обычной клавиатурой", replyKeyboard, null);

// Удаление клавиатуры
ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove(true);
Telegram.sendMessage(this, chatId, "Клавиатура удалена", keyboardRemove, null);
```

### Отправка фото
```java
// Отправка одного фото
Telegram.sendPhoto(this, chatId, "Описание фото", "https://example.com/photo.jpg", null, null);

// Отправка альбома фото
List<String> photoUrls = Arrays.asList(
    "https://example.com/photo1.jpg",
    "https://example.com/photo2.jpg",
    "https://example.com/photo3.jpg"
);
Telegram.sendPhoto(this, chatId, "Альбом фотографий", photoUrls, null);
```

### Отправка документа
```java
Telegram.sendDocument(this, chatId, "Документ PDF", "https://example.com/document.pdf", null, null);
```

### Оплата через Telegram Stars
```java
// Отправка инвойса в Stars (XTR)
List<LabeledPrice> prices = List.of(
    Telegram.starsPrice("Доступ на месяц", 50) // 50 Stars
);
Telegram.sendStarsInvoice(this, chatId, "Подписка", "1 месяц доступа", "payload_123", prices);

// Подтверждение pre-checkout запроса
if (update.hasPreCheckoutQuery()) {
    Telegram.answerPreCheckoutQuery(this, update.getPreCheckoutQuery().getId(), true, null);
}

// Обработка успешной оплаты
if (update.hasMessage() && update.getMessage().hasSuccessfulPayment()) {
    // TODO: выдать покупку
}
```

### Редактирование сообщения
```java
Telegram.editMessage(this, chatId, messageId, "Новый текст сообщения", keyboard);
```

### Удаление сообщения
```java
Telegram.deleteMessage(this, chatId, messageId);
```

### Работа с медиа-группами
```java
// Отправка альбома с фото и видео
List<String> mediaUrls = Arrays.asList(
    "https://example.com/photo1.jpg",
    "https://example.com/video1.mp4",
    "https://example.com/photo2.jpg"
);
Telegram.sendPhoto(this, chatId, "Медиа-альбом", mediaUrls, null);
```

### Отправка сообщений с форматированием HTML
```java
// Примеры HTML-форматирования
String formattedText = "Этот текст <b>жирный</b>, этот <i>курсивный</i>, "
    + "а это <code>моноширинный шрифт</code>.\n"
    + "Можно создавать <a href=\"https://telegram.org\">ссылки</a> и добавлять <s>зачеркнутый текст</s>.";
Telegram.sendMessage(this, chatId, formattedText, null, null);
```

### Обработка различных типов обновлений
```java
@Override
public void onUpdateReceived(Update update) {
    // Обработка текстовых сообщений
    if (update.hasMessage() && update.getMessage().hasText()) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        messageHandler(text, chatId, update.getMessage().getFrom().getUserName());
    } 
    // Обработка фото
    else if (update.hasMessage() && update.getMessage().hasPhoto()) {
        long chatId = update.getMessage().getChatId();
        List<PhotoSize> photos = update.getMessage().getPhoto();
        String fileId = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null).getFileId();
        // Обработка полученного фото
    } 
    // Обработка документов
    else if (update.hasMessage() && update.getMessage().hasDocument()) {
        long chatId = update.getMessage().getChatId();
        String fileId = update.getMessage().getDocument().getFileId();
        String fileName = update.getMessage().getDocument().getFileName();
        // Обработка полученного документа
    }
    // Обработка callback-запросов от inline-кнопок
    else if (update.hasCallbackQuery()) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();
        callbackHandler(update, callbackData, chatId);
    }
    // Обработка запросов на вступление в группу/канал
    else if (update.hasChatJoinRequest()) {
        long chatId = update.getChatJoinRequest().getUserChatId();
        // Обработка запроса на вступление
    }
}
```

## Создание FatJar для деплоя
Для создания исполняемого JAR-файла со всеми зависимостями выполните:

```bash
./gradlew fatJar
```

Созданный JAR-файл будет находиться в директории `build/libs/`.

### Логирование через SLF4J
Библиотека EazyTG имеет встроенную конфигурацию логирования через SLF4J и Logback. Вам не требуется дополнительная настройка, так как система логирования уже предварительно сконфигурирована в библиотеке.

Пример использования встроенного логирования:
```java
// Информационные сообщения
Logs.log("Бот запущен");
Logs.log(chatId, "Пользователь отправил сообщение");

// Сообщения об ошибках
Logs.error("Произошла ошибка при запуске бота");
Logs.error(chatId, "Ошибка при обработке сообщения пользователя");
```

Все логи содержат контекстную информацию:
- ID чата (если указан)
- Имя класса, из которого был вызван метод логирования
- Имя метода, из которого был вызван метод логирования

При необходимости переопределить конфигурацию логирования, создайте файл `logback.xml` в директории `src/main/resources` вашего проекта.

### Расширение функциональности ChatBot
Пример расширения базового класса ChatBot:

```java
public class MyCustomBot extends ChatBot {
    
    // Переопределение метода обработки сообщений
    @Override
    protected void messageHandler(String userText, long chatId, String userName) {
        // Ваша логика обработки сообщений
        
        // Проверка команд
        if (userText.startsWith("/start")) {
            sendWelcomeMessage(chatId, userName);
        } else if (userText.startsWith("/help")) {
            sendHelpMessage(chatId);
        } else {
            // Обработка обычных сообщений
            processUserMessage(userText, chatId, userName);
        }
    }
    
    // Пользовательские методы
    private void sendWelcomeMessage(long chatId, String userName) {
        String welcomeText = "Добро пожаловать, " + (userName != null ? userName : "пользователь") + "!";
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button("Помощь", "help"));
        buttons.add(new Button("О боте", "about"));
        
        InlineKeyboardMarkup keyboard = Telegram.createKeyboard(buttons, 2);
        Telegram.sendMessage(this, chatId, welcomeText, keyboard, null);
    }
    
    private void sendHelpMessage(long chatId) {
        String helpText = "Список доступных команд:\n" +
                          "/start - Начать работу с ботом\n" +
                          "/help - Показать это сообщение\n" +
                          "/about - Информация о боте";
        
        Telegram.sendMessage(this, chatId, helpText, null, null);
    }
    
    private void processUserMessage(String userText, long chatId, String userName) {
        // Ваша логика обработки пользовательских сообщений
        Telegram.sendMessage(this, chatId, "Вы написали: " + userText, null, null);
        
        // Пример логирования
        Logs.log(chatId, "Пользователь " + userName + " отправил сообщение: " + userText);
    }
}
```

## Структура проекта
- `ChatBot.java` - основной класс бота, обрабатывающий обновления
- `Main.java` - точка входа для запуска бота
- `lib/Button.java` - класс для создания кнопок
- `lib/Logs.java` - утилита для логирования
- `lib/Telegram.java` - основные методы для работы с Telegram API
- `lib/TelegramBotUtils.java` - вспомогательные утилиты

## Требования
- Java 8 или выше
- Gradle или Maven

## Зависимости
- org.telegram:telegrambots
- org.slf4j:slf4j-api

## Лицензия
MIT

## Автор
ohchillman

## Участие в разработке
Пул-реквесты приветствуются. Для серьезных изменений, пожалуйста, сначала откройте issue, чтобы обсудить предлагаемые изменения.
