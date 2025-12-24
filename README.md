# HTMX Spring Boot Voting App

A modern, interactive polling application built with Spring Boot 3, HTMX, and Thymeleaf. Create polls, vote, and view real-time results—all with dynamic updates and no page reloads.

## Features

- **Poll Management** - Create polls with multiple options from a clean, intuitive interface
- **Real-time Voting** - Vote on polls with instant feedback and result updates
- **Dynamic Results** - View live poll results with visual progress bars
- **Form Validation** - Modern HTML5 validation with custom CSS styling
- **Session-based Voting** - Prevent duplicate votes using browser session cookies
- **Responsive Design** - Works seamlessly on desktop, tablet, and mobile
- **HTMX Integration** - Smooth, dynamic interactions without page refreshes
- **Custom Dialogs** - Beautiful confirmation dialogs using native HTML `<dialog>` element

## Tech Stack

- **Backend**: Spring Boot 3.2.1, Java 21
- **Frontend**: HTML5, CSS3, HTMX 1.9.10
- **Templating**: Thymeleaf
- **Data Storage**: In-memory (thread-safe ArrayList)
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+

### Installation & Running

1. Clone the repository:
```bash
git clone https://github.com/WinitskyStan/htmx-spring-boot-voting.git
cd htmx-spring-boot-voting
```

2. Build and run the application:
```bash
mvn spring-boot:run
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

The app will automatically redirect to the management dashboard.

## Usage

### Creating Polls
1. Navigate to the home page
2. Enter a poll question
3. Add at least 2 options (use "+ Add Option" to add more)
4. Click "Create Poll"

### Voting
1. Click "Vote" on any poll in the list
2. Select your choice and click "Submit Vote"
3. View results instantly with live updates

### Viewing Results
- Click "Results" on any poll to see detailed voting statistics
- Results include vote counts, percentages, and visual progress bars

### Managing Polls
- Click "Delete" to remove a poll (a confirmation dialog will appear)
- Deleting a poll also removes all associated votes

## Project Structure

```
src/main/java/com/example/votingapp/
├── VotingAppApplication.java          # Main application entry point
├── RootController.java                # Root redirect handler
├── management/                        # Poll creation and management
│   ├── ManagementController.java
│   └── ManagementService.java
├── voting/                           # Voting functionality
│   ├── VotingController.java
│   ├── VotingService.java
│   └── VotingSessionService.java
├── results/                          # Results viewing
│   ├── ResultsController.java
│   └── ResultsService.java
└── shared/                           # Shared data models
    ├── Poll.java
    ├── PollOption.java
    ├── Vote.java
    └── PollStore.java

src/main/resources/
├── templates/                        # Thymeleaf templates
│   ├── management/management.html
│   ├── voting/voting.html
│   └── results/results.html
├── static/css/style.css             # Modern, responsive styling
└── application.properties             # Spring configuration
```

## Key Features in Detail

### Modern Form Validation
- Uses HTML5 `required` attribute with custom CSS styling
- `:user-invalid` pseudo-class shows validation errors only after user interaction
- Clean, professional error display without browser popups

### HTMX Integration
- `hx-get` for fetching additional options
- `hx-post` for form submissions
- `hx-delete` for poll deletion
- All with smooth DOM swaps and no full page reloads

### Custom Confirmation Dialog
- Beautiful centered modal using native HTML `<dialog>` element
- Semi-transparent backdrop
- Minimal JavaScript (just 10 lines)
- Replaces browser's default confirm dialog

### Session Management
- HttpOnly cookies prevent XSS attacks
- 30-day session expiration
- Prevents duplicate voting per session

## Architecture

The application follows a **package-by-feature** structure for better organization:
- Each feature (management, voting, results) is in its own package
- Shared models and services in a central `shared` package
- Clean separation of concerns with Controller → Service → Store layers

## Performance

- **Thread-safe in-memory storage** using `Collections.synchronizedList()`
- **No database queries** - instant CRUD operations
- **HTMX integration** - only necessary HTML fragments are sent
- **CSS-only animations** - smooth interactions without JavaScript overhead

## Browser Support

Works with all modern browsers supporting:
- HTML5 (required, form validation)
- CSS3 (modern selectors, flexbox)
- ES6+ JavaScript
- HTML `<dialog>` element (fallback for older browsers)

## Development

### Adding New Features
1. Create a new package in `src/main/java/com/example/votingapp/`
2. Add Controller (handles routing)
3. Add Service (business logic)
4. Add templates in `src/main/resources/templates/`

### Building for Production
```bash
mvn clean package
java -jar target/voting-app-1.0.0.jar
```

## Future Enhancements

Potential features for future versions:
- Database persistence (JPA/Hibernate)
- User authentication and authorization
- Poll categories and filtering
- Advanced analytics and charts
- Poll scheduling and expiration
- Duplicate vote prevention by IP address

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.
