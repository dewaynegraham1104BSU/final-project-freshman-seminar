# Final Project: Arcadia

## Description
Arcadia is a wellness app for Boise State students.
It helps users connect with their provider based on what they need by offering a simple, streamlined way to reach the right support.

## Team
- DeWayne Graham
- Jaidan Whitaker
- Osei Benu
- Eden Frazier
- Ma'Kyrin Baylor

## Live Demo
- Frontend: [LIVE_FRONTEND_URL]
- Backend: [LIVE_BACKEND_URL]

## Technology Stack
- Frontend: Node.js + Vite (npm run dev / npm run build)
- Backend: Java + Spring Boot (mvn spring-boot:run)
- API calls: Frontend -> Backend

## Prerequisites
Install:
- Java (required version: [e.g., 17])
- Maven
- Node.js + npm
- (Optional) Git

## Project Structure
- `backend/` Spring Boot backend
- `src/` (or your frontend directory) Vite frontend
- Root files: `package.json`, Vite config files, etc. (adjust if different)

## How to Run Locally

### 1) Start the Backend
```bash
cd backend
mvn spring-boot:run

### 2) Start the front end 
cd frontend
npm install
npm run dev
Front end should start at: http://localhost:5173/

How to Use

1. Open the frontend at: http://localhost:5173/

2. Use the app’s UI to enter what you need.

3. The app will send requests to the backend and display the results.

Troubleshooting

• Backend won’t start / port 8080 in use

• Stop the process using port 8080 and run again.

• Frontend won’t start / port 5173 in use

• Stop the process using port 5173 and run again.

• Frontend can’t reach backend

• Confirm the API URL matches http://localhost:8080.
