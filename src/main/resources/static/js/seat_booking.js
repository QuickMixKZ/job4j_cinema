const container = document.querySelector('.container');
const seats = document.querySelectorAll('.row .seat');
const count = document.getElementById('count');
const buttonBuy = document.getElementById('buttonBuy');


//Update total and count
function updateSelectedCount() {
    const selectedSeats = document.querySelectorAll('.row .seat.selected');
    const selectedSeatsCount = selectedSeats.length;
    count.innerText = selectedSeatsCount;
    if (selectedSeatsCount === 0) {
        buttonBuy.style.visibility = 'hidden';
    } else {
        buttonBuy.style.visibility = 'visible';
    }
}

//Seat click event
container.addEventListener('click', e => {
    if (e.target.classList.contains('seat') &&
        !e.target.classList.contains('occupied')) {
        e.target.classList.toggle('selected');
    }
    updateSelectedCount();
});

buttonBuy.addEventListener('click', () => {
    const pathArray = window.location.pathname.split('/');
    const movieId = pathArray[2];
    const sessionId = pathArray[3];
    const selectedSeats = document.querySelectorAll('.row .seat.selected');
    let tickets = [];
    selectedSeats.forEach((seat) => {
        let ticket = {
            session: {}
        };
        ticket.row = seat.parentNode.id;
        ticket.seat = seat.id;
        ticket.session.id = sessionId;
        ticket.session.movieId = movieId;
        tickets.push(ticket);
    });
     const jsonString = JSON.stringify(tickets);
     console.log(jsonString);
     jQuery.ajax ({
         url: window.location.origin + '/movies-today/buyTickets/',
         type: "POST",
         data: jsonString,
         dataType: "json",
         contentType: "application/json; charset=utf-8",
         statusCode: {
            202: function () {
                alert('Билеты куплены.');
                window.location.href = window.location.origin + '/profile/tickets';
            },
             400: function () {
                alert('Не удалось купить билет(ы), попробуйте ещё раз!');
                 location.reload();
             }
         }
     });
});

