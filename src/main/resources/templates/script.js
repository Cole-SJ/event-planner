// Wait for the document to be ready before executing the code
$(document).ready(function () {
    // Initialize the datepicker for the calendar element
    $("#calendar").datepicker({
        onSelect: function (dateText, inst) {
            // Call the function to show the event form when a date is selected
            showEventForm(dateText);
        },
        showOtherMonths: true, // Show dates from other months
        selectOtherMonths: true, // Allow selecting dates from other months
        showButtonPanel: true, // Show a button panel at the bottom
    });
});

// Function to show the event form with the selected date
function showEventForm(date) {
    // HTML code for the event form
    var formHtml = `
        <h2>Create Event</h2>
        <form id="event-form">
            <label for="eventName">Name of Event:</label>
            <input type="text" id="eventName" name="eventName" required><br>

            <label for="eventLocation">Location:</label>
            <input type="text" id="eventLocation" name="eventLocation" required>
            <div id="map"></div><br>

            <label for="eventDescription">Description:</label>
            <textarea id="eventDescription" name="eventDescription" required></textarea><br>

            <input type="submit" value="Submit">
        </form>
    `;

    // Inject the event form HTML into the specified container
    $("#event-form-container").html(formHtml);

    // Initialize Google Maps Autocomplete for the location input
    var input = document.getElementById('eventLocation');
    var autocomplete = new google.maps.places.Autocomplete(input);

    // Handle form submission
    $("#event-form").submit(function (event) {
        event.preventDefault();
        // Display an alert with the event details
        alert("Event created on " + date + "\n\nName: " + $("#eventName").val() + "\nLocation: " + $("#eventLocation").val() + "\nDescription: " + $("#eventDescription").val());
        // Reset the form after submission
        $("#event-form")[0].reset();
    });
}

// Function to show the sign-up form
function showSignUpForm() {
    // Hide login and forgot password forms, and show the sign-up form
    document.getElementById('login-form').classList.add('hidden');
    document.getElementById('signup-form').classList.remove('hidden');
    document.getElementById('forgot-password-form').classList.add('hidden');
}

// Function to show the forgot password form
function showForgotPasswordForm() {
    // Hide login and sign-up forms, and show the forgot password form
    document.getElementById('login-form').classList.add('hidden');
    document.getElementById('signup-form').classList.add('hidden');
    document.getElementById('forgot-password-form').classList.remove('hidden');
}

// Function to handle sign-up submission
function submitSignUp() {
    // Get the email, username, and password entered by the user
    var email = $("#newEmail").val();
    var username = $("#newUsername").val();
    var password = $("#newPassword").val();
    
    // Perform client-side validation (optional)
    if (!isValidEmail(email)) {
        alert("Please enter a valid email address.");
        return;
    }
    if (username.trim() === "") {
        alert("Please enter a username.");
        return;
    }
    if (password.trim() === "") {
        alert("Please enter a password.");
        return;
    }

    // Simulate a successful sign-up (for testing purposes)
    alert("Congratulations! You have successfully signed up with the following details:\n\nEmail: " + email + "\nUsername: " + username);

    // Clear the input fields after sign-up
    $("#newEmail").val("");
    $("#newUsername").val("");
    $("#newPassword").val("");
}


// Function to handle forgot password submission

function submitForgotPassword() {
    // Get the email entered by the user
    var email = $("#email").val();
    
    // Perform client-side validation of the email (optional)
    if (!isValidEmail(email)) {
        alert("Please enter a valid email address.");
        return;
    }

    // Simulate a successful password reset (for testing purposes)
    alert("An email with instructions to reset your password has been sent to " + email + ".");
}


// Function to validate email format
function isValidEmail(email) {
    // Regular expression for validating email format
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}
