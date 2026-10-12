# Quiz Application (React + Spring Boot + MySQL)

A beginner-friendly full-stack multiple-choice quiz project. It includes a React/CSS frontend, Spring Boot REST API, MySQL persistence, a 2-minute countdown, server-side scoring, and a results page.

## Requirements
- Java 21
- Maven 3.9+ (or use an IDE with Maven support)
- Node.js LTS and npm
- MySQL 8+
- Git

## 1. Set up MySQL
Open MySQL Workbench or a MySQL terminal and run:

```sql
CREATE DATABASE quiz_app;
CREATE USER 'quiz_user'@'localhost' IDENTIFIED BY 'change_me';
GRANT ALL PRIVILEGES ON quiz_app.* TO 'quiz_user'@'localhost';
FLUSH PRIVILEGES;
```

If `quiz_user` already exists, update its password with `ALTER USER`. You can instead use your existing MySQL username by setting `DB_USERNAME` and `DB_PASSWORD` environment variables. The sample password is for local development only; use a strong secret outside your machine.

## 2. Run the backend
From a terminal:

```bash
cd quiz-backend
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. On first startup it creates the tables and inserts one sample quiz with five questions. Hibernate `ddl-auto=update` is convenient for a student project; for production, use database migrations.

Windows PowerShell can set custom credentials for one session:

```powershell
$env:DB_USERNAME="quiz_user"
$env:DB_PASSWORD="your-local-password"
mvn spring-boot:run
```

## 3. Run the frontend
Open a second terminal:

```bash
cd quiz-frontend
npm install
```

Copy `.env.example` to `.env` and keep `VITE_API_URL=http://localhost:8080/api` for local development. Then:

```bash
npm run dev
```

Open the local URL printed by Vite, normally `http://localhost:5173`.

## API endpoints
- `GET /api/quizzes` — list quizzes
- `GET /api/quizzes/{quizId}/questions` — retrieve question text and options (never the correct answers)
- `POST /api/quizzes/{quizId}/submit` — submit `{ "answers": { "1": "B", "2": "C" } }` and receive a score

Question IDs are database-generated, so inspect the questions endpoint for the IDs if testing manually. The UI handles these IDs automatically. The backend calculates the score; the browser does not receive the answer key.

## Quick test checklist
1. Open `http://localhost:8080/api/quizzes`; expect a JSON list.
2. Open `http://localhost:8080/api/quizzes/1/questions`; expect questions with options but no `correctOption` field.
3. Start the frontend and start the quiz.
4. Select options, move back and forward, and finish.
5. Confirm results show correct, incorrect/skipped and percentage.
6. Refresh the browser and confirm the quiz list loads again.

## GitHub: separate repositories
Create two empty repositories on GitHub, e.g. `quiz-frontend` and `quiz-backend`. Do not add generated README files on GitHub if you plan to push these existing local folders directly.

For each folder, run these commands separately (replace the URL with your repository URL):

```bash
git init
git add .
git commit -m "Initial quiz application"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/quiz-frontend.git
git push -u origin main
```

Run the same sequence inside `quiz-backend`, changing the remote URL to `https://github.com/YOUR_USERNAME/quiz-backend.git`. If Git says the remote already exists, use `git remote set-url origin YOUR_REPOSITORY_URL`. Never commit passwords or `.env` files.

## Deployment overview
### Frontend (Vercel or Netlify)
- Import the `quiz-frontend` GitHub repository.
- Build command: `npm run build`.
- Output directory: `dist`.
- Set environment variable `VITE_API_URL` to the deployed backend base URL plus `/api`, for example `https://YOUR-BACKEND-HOST/api`.
- Redeploy after changing environment variables.

### Backend (Render or another Java-capable host)
- Deploy the `quiz-backend` repository as a Java/Maven web service.
- Build command: `mvn clean package`.
- Start command: `java -jar target/quiz-backend-0.0.1-SNAPSHOT.jar`.
- Configure environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `FRONTEND_ORIGIN`.
- `DB_URL` should be the hosting provider's MySQL JDBC URL, including database name and required connection parameters. Use a managed MySQL provider that permits connections from your backend host. Do not expose the database publicly without appropriate access controls.
- Set `FRONTEND_ORIGIN` to the exact deployed frontend origin, e.g. `https://your-quiz-site.vercel.app` (no trailing slash).
- Confirm the backend host supports Java 21 and a persistent web service. Free hosting availability, sleep behavior, and database offerings change; check the provider's current terms.

After deployment, test the public frontend and the backend's `/api/quizzes` endpoint. If the browser reports a CORS error, verify `FRONTEND_ORIGIN` matches the frontend origin exactly. If the API cannot connect to MySQL, verify the JDBC URL, credentials, database network access, and TLS settings required by your provider.

## Scope / limitations
This starter includes a single seeded sample quiz and public endpoints. It does not include login, admin tools, saved attempts, or a quiz-authoring screen. Those can be added later. The timer is a frontend timer; if strict server-enforced time limits are required, store an attempt start time on the backend and validate the deadline during submission.
