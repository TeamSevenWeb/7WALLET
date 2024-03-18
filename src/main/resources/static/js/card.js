// var closeModalBtn = document.getElementsByClassName("close");

/*Flyout for cards*/
var modalOverlay = document.getElementById("modalOverlay");

var cardFlyout = document.getElementById('cardFlyout');
function showFlyout() {
    cardFlyout.style.display = 'block';
    modalOverlay.style.display = "block"; // Show the overlay
}

document.addEventListener('DOMContentLoaded', function() {
    // Check if the user has no cards and display the flyout accordingly
    if(userCardsEmpty) {
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
document.getElementById('openFlyout').addEventListener('click', function(event) {
    // Check if the clicked element is the "Add Card" button
    if (event.target.matches('button')) {
        // Display the card details flyout
        showFlyout();
    }
});

document.getElementById('cardForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Collect card details
    var cardNumber = document.getElementById('number').value;
    var holder = document.getElementById('holder').value;
    var cvv = document.getElementById('cvv').value;
    var expiryDate = document.getElementById('expiryDate').value;

    // Send request to save card details
    // Example using fetch API
    fetch('/api/users/cards', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : username +" "+password
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