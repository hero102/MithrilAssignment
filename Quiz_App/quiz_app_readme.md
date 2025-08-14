# Quiz Application

A responsive web-based Quiz Application built with **Java Servlets**, **HTML**, **CSS**, and **Bootstrap**. This project allows users to take quizzes, track attempts, and view their dashboard with scores and progress.

---

## Features

- **User Authentication**

  - Secure login and registration
  - Error handling for incorrect credentials

- **Quiz Functionality**

  - Timed questions with visual progress bar
  - Animated and interactive question options
  - Previous and Next navigation
  - Automatic submission when time expires

- **Dashboard**

  - Displays previous attempts with serial numbers
  - Shows highest score
  - Responsive design for all screen sizes

- **Enhanced UI**

  - Gradient animated backgrounds
  - Pulse animation and beep sound in last 5 seconds of timer
  - Hover effects and glow for selected options
  - Responsive and modern card-style layout

- **Database Integration**

  - Stores user credentials securely
  - Saves quiz attempts with score and timestamp
  - Tracks progress across multiple quiz sessions

---

## Technology Stack

- **Backend:** Java Servlets (Jakarta EE)
- **Frontend:** HTML, CSS, Bootstrap 5, JavaScript
- **Database:** MySQL / MariaDB (via JDBC)
- **Tools:** Eclipse/IntelliJ, Apache Tomcat

---

## Installation & Setup

1. Clone the repository:

```bash
git clone https://github.com/your-username/quiz-application.git
```

2. Import the project in your IDE (Eclipse/IntelliJ) as a Maven or Dynamic Web Project.

3. Configure your **database**:

   - Create a database `quiz_app`
   - Run SQL scripts to create `users`, `questions`, `quiz_attempts` tables.

4. Update **DBUtil.java** with your database connection credentials.

5. Deploy the project to **Apache Tomcat** (or any Jakarta EE compatible server).

6. Access the application in your browser:

```
http://localhost:8080/Quiz_Application/
```

---

## Usage

1. Register a new account or login with existing credentials.
2. Start a quiz from the Home page.
3. Select answers for each question within the time limit.
4. View your previous attempts and highest score in the dashboard.

---

## Screenshots

&#x20;&#x20;

---

## License

This project is licensed under the MIT License.

---

## Author

**Prashant Kumar Pandey**

- GitHub: [SEA133\_Prashantkumar](https://github.com/SEA133_Prashantkumar)
- LinkedIn: [linkedin.com/in/prashant-pandey](https://www.linkedin.com/in/prashant-pandey/)

