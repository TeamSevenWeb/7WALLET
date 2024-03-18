//NavBar
function hideIconBar(){
    var iconBar = document.getElementById("iconBar");
    var navigation = document.getElementById("navigation");
    iconBar.setAttribute("style", "display:none;");
    navigation.classList.remove("hide");
}

function showIconBar(){
    var iconBar = document.getElementById("iconBar");
    var navigation = document.getElementById("navigation");
    iconBar.setAttribute("style", "display:block;");
    navigation.classList.add("hide");
}

//Comment
function showComment(){
    var commentArea = document.getElementById("comment-area");
    commentArea.classList.remove("hide");
}

//Reply
function showReply(){
    var replyArea = document.getElementById("reply-area");
    replyArea.classList.remove("hide");
}


window.addEventListener('DOMContentLoaded', event => {

    // Navbar shrink function
    var navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav');
        if (!navbarCollapsible) {
            return;
        }
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink')
        } else {
            navbarCollapsible.classList.add('navbar-shrink')
        }

    };

    // Shrink the navbar
    navbarShrink();

    // Shrink the navbar when page is scrolled
    document.addEventListener('scroll', navbarShrink);

    // Activate Bootstrap scrollspy on the main nav element
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            offset: 72,
        });
    };

    // Collapse responsive navbar when toggler is visible
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });

});
/*Close button*/
var closeModalBtn = document.getElementsByClassName("close");

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
window.addEventListener("click", function(event) {
    if (event.target !== cardFlyout) {
        modal.style.display = "none"; // Hide the modal
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
            'Authorization' : "alex.m ForumSeven"
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


document.getElementById('walletForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Collect wallet details
    var walletName = document.getElementById('name').value;

    // Send request to wallet details
    // Example using fetch API
    fetch('/api/join wallet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authentication' : "alex.m ForumSeven"
        },
        body: JSON.stringify({
            name: walletName
        })
    }).then(response => {
        if (response.ok) {
            alert('Wallet created successfully!');
            // Close the flyout after saving
            document.getElementById('cardFlyout').style.display = 'none';
            location.reload();
        } else {
            alert('Error creating wallet. Please check your details and try again.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});

// Function to display the success flyout
// function showSuccessFlyout() {
//     document.getElementById('successFlyout').style.display = 'block';
// }

// Function to hide the success flyout
// function hideSuccessFlyout() {
//     document.getElementById('successFlyout').style.display = 'none';
// }

// Handle form submission to perform the transfer
document.getElementById('transferForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    // Simulate transfer (replace with actual transfer logic)
    const transferAmount = document.getElementById('amount').value;
    const cardId = document.getElementById('cardId').value;

    // Simulate API request to perform transfer
    fetch('/api/wallet/fund', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': "alex.m ForumSeven"
        },
        body: JSON.stringify( {
            cardId : cardId,
            amount : transferAmount
        })
    }).then(response => {
        if (response.ok) {
            // Show success flyout
            alert('Transfer successful!');

            // showSuccessFlyout();
            // Hide success flyout after a certain time (optional)

            // setTimeout(hideSuccessFlyout, 5000); // Hide after 5 seconds
        } else {
            alert('Transfer failed. Please try again.');
        }
    }).catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
});


//open modals
// Get the button element
var openModalBtn = document.getElementById("openModalBtn");

// Get the modal element
var modal = document.getElementById("myModal");

// When the user clicks the button, open the modal
openModalBtn.addEventListener("click", function() {
    modal.style.display = "block"; // Show the modal
});

// When the user clicks anywhere outside of the modal, close it
window.addEventListener("click", function(event) {
    if (event.target === modal) {
        modal.style.display = "none"; // Hide the modal
    }
});