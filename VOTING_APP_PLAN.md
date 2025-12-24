# Voting App Implementation Plan

## Overview
A simple polling application using HTMX, HTML-first development with inline fragments, and package-by-feature architecture. Users can create polls, vote on them, and view live results.

## Technology Stack
- Spring Boot 3.2.1
- Java 21
- Thymeleaf
- HTMX 1.9.10 (via CDN)
- In-memory storage (ArrayList)
- Minimal CSS (no frameworks)

## Core Features
- **Poll Management**: List all polls, create new polls, delete polls
- **Single Poll Voting**: Vote on individual polls with session-based duplicate prevention
- **Live Results**: View results for individual polls with auto-updating counts
- **Session Tracking**: Cookie-based voting prevention (no authentication)

## Package Structure

```
src/main/java/com/example/votingapp/
├── VotingAppApplication.java
├── shared/
│   ├── Poll.java                    // Domain model
│   ├── PollOption.java              // Domain model
│   ├── Vote.java                    // Domain model
│   └── PollStore.java               // In-memory ArrayList storage
├── management/
│   ├── ManagementController.java    // List all polls, create, delete
│   └── ManagementService.java       // Business logic
├── voting/
│   ├── VotingController.java        // Show single poll, handle vote
│   ├── VotingService.java           // Vote handling logic
│   └── VotingSessionService.java    // Cookie-based session tracking
└── results/
    ├── ResultsController.java       // Show single poll results
    └── ResultsService.java          // Calculate vote percentages

src/main/resources/
├── templates/
│   ├── management/
│   │   └── management.html          // All management UI with inline fragments
│   ├── voting/
│   │   └── voting.html              // Single poll voting with inline fragments
│   └── results/
│       └── results.html             // Single poll results with inline fragments
└── static/
    └── css/
        └── style.css                 // Minimal styling
```

## Data Models

### Poll.java
```java
public class Poll {
    private Long id;
    private String question;
    private List<PollOption> options;
    private LocalDateTime createdAt;
}
```

### PollOption.java
```java
public class PollOption {
    private Long id;
    private String text;
    private int voteCount;
}
```

### Vote.java
```java
public class Vote {
    private String sessionId;
    private Long pollId;
    private Long optionId;
    private LocalDateTime votedAt;
}
```

### PollStore.java
```java
@Component
public class PollStore {
    private final List<Poll> polls = Collections.synchronizedList(new ArrayList<>());
    private final List<Vote> votes = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong pollIdCounter = new AtomicLong(1);
    private final AtomicLong optionIdCounter = new AtomicLong(1);

    // Thread-safe CRUD operations
    // No repositories - just ArrayList operations
}
```

## Routes

### Management (`/management`)
- **GET** `/management` → Full page listing all polls
- **POST** `/management/polls` → Create poll, returns `poll-list` fragment
- **DELETE** `/management/polls/{id}` → Delete poll, returns `poll-list` fragment
- **GET** `/management/option-input` → Returns `option-input` fragment

### Voting (`/voting/{pollId}`)
- **GET** `/voting/{pollId}` → Full page showing single poll with vote form
- **POST** `/voting/{pollId}/vote` → Submit vote, returns `poll-card` fragment

### Results (`/results/{pollId}`)
- **GET** `/results/{pollId}` → Full page showing single poll results
- **GET** `/results/{pollId}/live` → Returns `poll-results` fragment (for HTMX polling)

## Template Design - Inline Fragments

### management/management.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Poll Management</title>
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <main>
        <h1>Manage Polls</h1>

        <!-- Poll list with inline fragment -->
        <div id="poll-list" th:fragment="poll-list">
            <table>
                <thead>
                    <tr>
                        <th>Question</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr th:each="poll : ${polls}">
                        <td th:text="${poll.question}"></td>
                        <td th:text="${poll.createdAt}"></td>
                        <td>
                            <a th:href="@{/voting/{id}(id=${poll.id})}">Vote</a>
                            <a th:href="@{/results/{id}(id=${poll.id})}">Results</a>
                            <button hx-delete="@{/management/polls/{id}(id=${poll.id})}"
                                    hx-target="#poll-list"
                                    hx-swap="outerHTML"
                                    hx-confirm="Delete this poll?">
                                Delete
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <h2>Create New Poll</h2>

        <!-- Create poll form -->
        <form hx-post="/management/polls"
              hx-target="#poll-list"
              hx-swap="outerHTML">
            <input type="text" name="question" placeholder="Poll question" required>
            <div id="options-container">
                <input type="text" name="options[]" placeholder="Option 1" required>
                <input type="text" name="options[]" placeholder="Option 2" required>
            </div>
            <button type="button"
                    hx-get="/management/option-input"
                    hx-target="#options-container"
                    hx-swap="beforeend">
                Add Option
            </button>
            <button type="submit">Create Poll</button>
        </form>

        <!-- Additional option input fragment -->
        <input type="text" name="options[]" placeholder="Option"
               th:fragment="option-input">
    </main>
</body>
</html>
```

### voting/voting.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Vote</title>
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <main>
        <nav><a href="/management">← Back to all polls</a></nav>

        <!-- Poll card with inline fragment -->
        <div id="poll-card" th:fragment="poll-card">
            <h1 th:text="${poll.question}"></h1>

            <!-- Show voting form if not voted -->
            <form th:if="${!hasVoted}"
                  th:action="@{/voting/{id}/vote(id=${poll.id})}"
                  hx-post
                  hx-target="#poll-card"
                  hx-swap="outerHTML">
                <div th:each="option : ${poll.options}">
                    <label>
                        <input type="radio" name="optionId"
                               th:value="${option.id}" required>
                        <span th:text="${option.text}"></span>
                    </label>
                </div>
                <button type="submit">Submit Vote</button>
            </form>

            <!-- Show confirmation if voted -->
            <div th:if="${hasVoted}">
                <p>✓ You voted for: <strong th:text="${votedOption}"></strong></p>
                <p>
                    <a th:href="@{/results/{id}(id=${poll.id})}">View Results</a> |
                    <a href="/management">Back to all polls</a>
                </p>
            </div>
        </div>
    </main>
</body>
</html>
```

### results/results.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Results</title>
    <script src="https://unpkg.com/htmx.org@1.9.10"></script>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <main>
        <nav><a href="/management">← Back to all polls</a></nav>

        <!-- Results card with inline fragment for live updates -->
        <div id="poll-results"
             th:fragment="poll-results"
             hx-get="@{/results/{id}/live(id=${poll.id})}"
             hx-trigger="every 3s"
             hx-swap="outerHTML">

            <h1 th:text="${poll.question}"></h1>
            <p>Total votes: <span th:text="${totalVotes}">0</span></p>

            <div th:each="option : ${poll.options}" class="result-row">
                <div class="result-label">
                    <span th:text="${option.text}"></span>
                    <span th:text="${option.voteCount} + ' votes'">0 votes</span>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill"
                         th:style="'width: ' + ${option.percentage} + '%'">
                    </div>
                </div>
                <span th:text="${option.percentage} + '%'">0%</span>
            </div>

            <p>
                <a th:href="@{/voting/{id}(id=${poll.id})}">Go vote</a> |
                <a href="/management">Back to all polls</a>
            </p>
        </div>
    </main>
</body>
</html>
```

## Key HTMX Patterns

### 1. Vote Submission
```html
<form hx-post="/voting/{pollId}/vote"
      hx-target="#poll-card"
      hx-swap="outerHTML">
```
After voting, server returns updated poll-card fragment showing confirmation.

### 2. Live Results Updates
```html
<div hx-get="/results/{pollId}/live"
     hx-trigger="every 3s"
     hx-swap="outerHTML">
```
Fragment auto-refreshes every 3 seconds to show updated vote counts.

### 3. Dynamic Form Fields
```html
<button hx-get="/management/option-input"
        hx-target="#options-container"
        hx-swap="beforeend">
```
Add new poll option input fields without page reload.

### 4. Delete with Confirmation
```html
<button hx-delete="/management/polls/{id}"
        hx-target="#poll-list"
        hx-confirm="Delete this poll?">
```
Native browser confirmation before deletion.

## Session Management

### VotingSessionService
```java
@Service
public class VotingSessionService {
    private static final String COOKIE_NAME = "voting-session-id";

    // Generate UUID on first visit, store in cookie
    // Check if session has voted on poll
    // Cookie: HttpOnly, 30-day expiry
}
```

## Initial Sample Polls

Seed 3 polls on application startup:
1. "What's your favorite programming language?" (Java, Python, JavaScript, Rust, Go)
2. "Preferred development environment?" (VS Code, IntelliJ IDEA, Vim, Emacs)
3. "Best HTMX feature?" (hx-get, hx-post, hx-swap, hx-trigger, hx-target)

## CSS Strategy

Minimal styling with:
- Simple CSS variables for colors
- Basic layout using flexbox
- Poll cards with borders and padding
- CSS-only progress bars using width percentages
- Responsive without media queries
- Clean, readable typography

## User Flow

1. **Management Page** (`/management`)
   - See all polls in a table
   - Create new poll with dynamic option fields
   - Click "Vote" → Navigate to `/voting/{pollId}`
   - Click "Results" → Navigate to `/results/{pollId}`
   - Delete polls with confirmation

2. **Voting Page** (`/voting/{pollId}`)
   - View single poll question
   - Select one option and submit
   - If already voted: see confirmation message
   - Link to view results or go back

3. **Results Page** (`/results/{pollId}`)
   - View single poll results with vote counts
   - See percentage bars (auto-updating every 3s)
   - Link to vote or go back to management

## Implementation Principles

✅ **Package by Feature** - Each feature is self-contained
✅ **Inline Fragments** - Defined in same template where used
✅ **HTML-First** - No JavaScript required
✅ **HTMX for Interactivity** - Forms, polling, dynamic updates
✅ **In-Memory Storage** - Simple ArrayList, no database
✅ **Session Cookies** - Prevent duplicate voting
✅ **Minimal CSS** - Clean and functional

## Testing Strategy

- Integration tests using `@SpringBootTest` and `TestRestTemplate`
- Test poll creation and deletion
- Test voting flow and duplicate prevention
- Test results calculation
- Test session cookie handling

## Future Enhancements (Out of Scope)

- Poll expiration dates
- Edit existing polls
- Vote history per session
- Export results to CSV
- Database persistence
- User authentication
- Multiple choice voting
