// var closeModalBtn = document.getElementsByClassName("close");
function removeSpaces() {
    var input = document.getElementById("number");
    var value = input.value.replace(/\s/g, ''); // Remove existing spaces
    var formattedValue = '';
    for (var i = 0; i < value.length; i++) {
        if (i > 0 && i % 4 === 0) {
            formattedValue += ' '; // Add a space after every fourth character
        }
        formattedValue += value.charAt(i);
    }
    input.value = formattedValue;
    input.setAttribute('data-value', value); // Set a data attribute to store the actual value
}

function getActualValue() {
    var input = document.getElementById("number");
    return input.getAttribute('data-value');
}

function formatCVV() {
    var input = document.getElementById("cvv");
    var value = input.value.replace(/\D/g, ''); // Remove non-numeric characters

    // If the length is greater than 2, insert a slash after the second character
    if (value.length > 3) {
        value = value.substr(0, 3);
    }

    input.value = value;
}

function formatExpiryDate() {
    var input = document.getElementById("expiryDate");
    var value = input.value.replace(/\D/g, ''); // Remove non-numeric characters

    // If the length is greater than 2, insert a slash after the second character
    if (value.length > 2) {
        value = value.substr(0, 2) + '/' + value.substr(2);
    }

    // If the length is greater than 5, truncate the value to ensure only "MM/YY" format
    if (value.length > 5) {
        value = value.substr(0, 5);
    }

    input.value = value;
}

function validateExpiryDate() {
    var expiryDateInput = document.getElementById("expiryDate");
    var expiryDateError = document.getElementById("expiryDateError");
    var value = expiryDateInput.value.trim();


    // Extract month and year from the input value
    var parts = value.split('/');
    var month = parseInt(parts[0]);
    var year = parseInt(parts[1]);

    // Get the current date
    var currentDate = new Date();
    var currentYear = currentDate.getFullYear() % 100; // Get the last two digits of the current year
    var currentMonth = currentDate.getMonth() + 1; // Get the current month (1-indexed)

    // Verify that the month is valid (between 1 and 12) and the year is not in the past
    var isValid = month >= 1 && month <= 12 && !(year < currentYear || (year === currentYear && month < currentMonth));

    if (isValid || expiryDateInput.value==="") {
        expiryDateError.style.display = 'none'; // Hide error message if date is valid
    } else if (!isValid) {
        // Show error message only if the field has been interacted with
        expiryDateError.style.display = 'inline'; // Show error message if date is invalid and field has been interacted with
        expiryDateInput.value = "";
    }
}

/*Flyout for cards*/
var modalOverlay = document.getElementById("modalOverlay");

var cardFlyout = document.getElementById('cardFlyout');

function showFlyout() {
    cardFlyout.style.display = 'block';
    modalOverlay.style.display = "block"; // Show the overlay
}

document.addEventListener('DOMContentLoaded', function () {
    // Check if the user has no cards and display the flyout accordingly
    if (userCardsEmpty) {
        showFlyout();
    }
});

modalOverlay.addEventListener("click", closeModal);
for (var i = 0; i < closeModalBtn.length; i++) {
    closeModalBtn[i].addEventListener("click", closeModal);
}

function closeModal() {
    cardFlyout.style.display = "none"; // Hide the modal
    modalOverlay.style.display = "none"; // Hide the overlay
}

// Event listener for clicks on the "Add Card" button
document.getElementById('openFlyout').addEventListener('click', function (event) {
    // Check if the clicked element is the "Add Card" button
    if (event.target.matches('button')) {
        // Display the card details flyout
        showFlyout();
    }
});

document.getElementById('cardForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent form submission

    // Collect card details
    var cardNumber = getActualValue();
    var holder = document.getElementById('holder').value;
    var cvv = document.getElementById('cvv').value;
    var expiryDate = document.getElementById('expiryDate').value;

    // Send request to save card details
    // Example using fetch API
    fetch('/api/users/cards', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication': username + " " + password
        },
        body: JSON.stringify({
            number: cardNumber,
            holder: holder,
            cvv: cvv,
            expirationDate: expiryDate
        })
    }).then(response => {
        if (response.ok) {
            alert('Card saved successfully!');
            // Close the flyout after saving
            document.getElementById('cardFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error saving card. Please check your details and try again.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});