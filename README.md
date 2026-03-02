# YesNoQuest 🎯

### A Kotlin + Spring Boot Interactive Email Web Application

------------------------------------------------------------------------

## 📌 Project Description

YesNoQuest is a Kotlin-based Spring Boot web application that allows
users to:

-   Register and authenticate securely using Spring Security
-   Generate a custom Yes/No interactive email
-   Send the email using JavaMailSender
-   Include a playful "No" button trap (dynamic HTML interaction)
-   Persist user data using an H2 database
-   Securely inject email credentials using environment variables

This project demonstrates full-stack backend development using modern
JVM technologies and secure runtime configuration practices.

------------------------------------------------------------------------

## 🛠 Technology Stack

-   Kotlin
-   Spring Boot
-   Gradle
-   Spring Security
-   Mustache Templates
-   H2 Database
-   JavaMailSender

------------------------------------------------------------------------

## 🔐 Security & Environment Configuration

This application **does NOT store email credentials in the codebase**.

Instead, it uses environment variables:

-   `MAIL_USER`
-   `MAIL_PASS`

Spring Boot reads these values at runtime from:

    spring.mail.username=${MAIL_USER}
    spring.mail.password=${MAIL_PASS}

This prevents credential exposure in GitHub repositories.

------------------------------------------------------------------------

# 🚀 How to Run the Project (Terminal Method)

These steps are designed for someone running the project directly from
the terminal.

------------------------------------------------------------------------
# 📦 Version Requirements

To run this project successfully, ensure the following versions are installed:

- Java 17 or 21

- Gradle 8.14.3 or later (required for Spring Boot 4.1.0-M2)

- Spring Boot 4.1.0-M2

- H2 Database (embedded, no setup required)

The project includes a Gradle Wrapper, so a global Gradle installation is not required.
------------------------------------------------------------------------

## 1️⃣ Clone the Repository

``` bash
git clone https://github.com/yourusername/YesNoQuest.git
cd YesNoQuest
```

------------------------------------------------------------------------

## 2️⃣ Provide Email Credentials (Session-Based Variables)

### 🔐 Generate a Gmail App Password (Required for Email Sending)

You have to use you Gmail, you must generate an App Password.
Do NOT use your regular Gmail password.

#### Step 1: Enable 2-Step Verification

Go to: https://myaccount.google.com/security

Under “Signing in to Google”, click 2-Step Verification

Follow the steps to enable it

⚠️ App Passwords will not appear unless 2-Step Verification is enabled.

#### Step 2: Generate an App Password

1. Go to: https://myaccount.google.com/apppasswords
   (It won't work if you are using your college email!)
2. Select:
   - App: Mail
   - Device: Other (Custom)

3. Enter a name (e.g., YesNoQuest)

4. Click Generate

5. Google will provide a 16-character password

Copy this password — this is what you will use as:

``` bash
MAIL_PASS = "your_app_password"
```

------------------------------------------------------------------------

### 🔹 Windows (PowerShell)

From inside the project directory:

``` powershell
$env:MAIL_USER="your_email@gmail.com"
$env:MAIL_PASS="your_app_password"
./gradlew clean bootRun
```

Example:

``` powershell
$env:MAIL_USER="iamyouremail@gmail.com"
$env:MAIL_PASS="sadfasdfasdfasdf" (REMOVE EVERY SPACE IN THE GIVEN PASSWORD)
./gradlew clean bootRun
```

These variables only exist for that PowerShell session.

------------------------------------------------------------------------

### 🔹 Mac / Linux (bash or zsh)

``` bash
export MAIL_USER="your_email@gmail.com"
export MAIL_PASS="your_app_password"
./gradlew clean bootRun
```

Or inline:

``` bash
MAIL_USERNAME="your_email@gmail.com" MAIL_PASSWORD="your_app_password" ./gradlew clean bootRun
```

------------------------------------------------------------------------

## 3️⃣ Access the Application

Once the application starts successfully, open:

    http://localhost:8080

------------------------------------------------------------------------

## 🗄 H2 Database Console (Optional)

If enabled, access:

    http://localhost:8080/h2-console

Typical settings:

- JDBC URL: jdbc:h2:mem:yesnoquestdb
- Username: sa
- Password: (leave blank)

------------------------------------------------------------------------

## 📁 Project Structure

    src/
     ├── main/
     │    ├── kotlin/        # Application logic
     │    ├── resources/
     │         ├── templates/   # Mustache templates
     │         ├── application.properties
     └── test/

------------------------------------------------------------------------

## 🧪 Common Issues & Troubleshooting

### ❌ Email Not Sending

-   Verify environment variables are set
-   Ensure Gmail App Password is correct
-   Confirm SMTP port 587 is open

### ❌ Port 8080 Already in Use

Change in `application.properties`:

    server.port=8081

### ❌ Gradle Not Recognized

Use the wrapper:

    ./gradlew

------------------------------------------------------------------------

## 💡 Why Environment Variables?

-   Prevents credential leaks
-   Keeps repository secure
-   Follows industry best practices
-   Makes the project safe for public GitHub hosting

------------------------------------------------------------------------

## 👨🏽‍💻 Author

Andrias Zelele\
Carroll College -- Computer Science

------------------------------------------------------------------------

## 🤖 Acknowledgements

This project was built by Andrias Zelele.\
ChatGPT (OpenAI) was used as a development assistant for architecture
clarification, debugging guidance, and Spring Boot configuration
support.\
All final implementation decisions and integration were completed by the
author.

------------------------------------------------------------------------

## 📌 License

This project is for educational and portfolio purposes.
