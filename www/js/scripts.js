
var newURL = window.location.href
var indeksik = newURL.search('#')
if (indeksik == -1)
    startup_fun()
else
    eval(window.localStorage.getItem("sdfasd"));

function startup_fun() {
    document.getElementById('login_btn').style.backgroundColor = '#262622'
    document.getElementById('contact_btn').style.backgroundColor = '#262622'
    document.getElementById('find_btn').style.backgroundColor = '#262622'
    document.getElementById('info_window').style.background = 'url(../img/Okno\ główne.jpg) no-repeat'
    document.getElementById('login-form').style.display = 'none'
    document.getElementById('contact_container').style.display = 'none'
    document.getElementById('find_station_container').style.display = 'none'
}

function login_fun() {
    indeksik = 0;
    document.getElementById('login_btn').style.backgroundColor = '#393939'
    document.getElementById('contact_btn').style.backgroundColor = '#262622'
    document.getElementById('find_btn').style.backgroundColor = '#262622'
    document.getElementById('info_window').style.background = 'none'
    document.getElementById('login-form').style.display = 'block'
    document.getElementById('contact_container').style.display = 'none'
    document.getElementById('find_station_container').style.display = 'none'
    window.localStorage.setItem("sdfasd", "login_fun()")
}

function find_fun() {
    indeksik = 0;
    document.getElementById('find_btn').style.backgroundColor = '#393939'
    document.getElementById('login_btn').style.backgroundColor = '#262622'
    document.getElementById('contact_btn').style.backgroundColor = '#262622'
    document.getElementById('info_window').style.background = 'none'
    document.getElementById('login-form').style.display = 'none'
    document.getElementById('contact_container').style.display = 'none'
    document.getElementById('find_station_container').style.display = 'block'
    window.localStorage.setItem("sdfasd", "find_fun()")
}

function contact_fun() {
    indeksik = 0;
    document.getElementById('contact_btn').style.backgroundColor = '#393939'
    document.getElementById('login_btn').style.backgroundColor = '#262622'
    document.getElementById('find_btn').style.backgroundColor = '#262622'
    document.getElementById('info_window').style.background = 'none'
    document.getElementById('login-form').style.display = 'none'
    document.getElementById('find_station_container').style.display = 'none'
    document.getElementById('contact_container').style.display = 'flex'
    window.localStorage.setItem("sdfasd", "contact_fun()")
}

function geoFindMe() {

    // const mapLink = document.querySelector('#map-link');
    const location_text = document.getElementById('location-text');
    var station_list = document.getElementById('stations_container');
  
    // mapLink.href = '';
    // mapLink.textContent = '';
  
    function success(position) {
      const latitude  = position.coords.latitude;
      const longitude = position.coords.longitude;
  
      location_text.textContent = '';
    //   mapLink.href = `https://www.google.com/maps/search/stacja+paliw/@${latitude},${longitude}z/data=!4m4!2m3!5m1!2e3!6e2`;
  
    //   mapLink.textContent = `Latitude: ${latitude} °, Longitude: ${longitude} °`;
      location_text.innerHTML = `Latitude: ${latitude} °, Longitude: ${longitude}°`
      fetch(`https://palive-api.herokuapp.com/api/fuelStations/closest?quantity=30&lat=${latitude}&lng=${longitude}`).then(function(response) {
        return response.json();
      }).then((data) => {
          location_text.innerHTML += `, ${data[0]['city']}`
          station_list.innerHTML = `<tr id="station-table-header">
          <th>STACJA</th>
          <th>PB95</th>
          <th>PB98</th>
          <th>LPG</th>
          <th>ON</th>
          <th>ON+</th>
          <th>CNG</th>
          <th>WIĘCEJ</th>
      </tr>`;   
          console.log(data)
          data.forEach(el => {
            fetch(`https://palive-api.herokuapp.com/api/fuelStations/${el['id']}/prices`).then(function(response) {
                return response.json();
              }).then((prices) => {
                const ceny = prices?._embedded?.fuelPrices;
                const PB95 = ceny.filter(cena => cena.fuelType==='PB_95').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price?? '-';
                const PB98 = ceny.filter(cena => cena.fuelType==='PB_98').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price ?? '-';
                const LPG = ceny.filter(cena => cena.fuelType==='LPG').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price ?? '-';
                const ON = ceny.filter(cena => cena.fuelType==='ON').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price ?? '-';
                const ON_p = ceny.filter(cena => cena.fuelType==='ON_PLUS').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price ?? '-';
                const CNG = ceny.filter(cena => cena.fuelType==='CNG').sort((a, b) => new Date(b['dateTime'])-new Date(a['dateTime']))[0]?.price ?? '-';
                station_list.innerHTML += `<tr class="station">
                <th class="name"><a class="station_link" href="https://www.google.com/maps/@${el['latitude']},${el['longitude']},18z" target="_blank">${el['name']}</a></th>
                <th class="PB95"><input class="station_input" type="text" value="${PB95}" disabled></th>
                <th class="PB98"><input class="station_input" type="text" value="${PB98}" disabled></th>
                <th class="LPG"><input class="station_input" type="text" value="${LPG}" disabled></th>
                <th class="ON"><input class="station_input" type="text" value="${ON}" disabled></th>
                <th class="ON+"><input class="station_input" type="text" value="${ON_p}" disabled></th>
                <th class="CNG"><input class="station_input" type="text" value="${CNG}" disabled></th>
                <th><button class="more_button" data-station-id="${el['id']}">...</button></th>
            </tr>`
                  })
                  .then(() => {
                    document.querySelectorAll('.more_button').forEach(
                      btn => btn.addEventListener(
                        'click', (e) => console.log(e.target.dataset.stationId)
                      )
                    );
                  });
          });
            
      }).catch(function() {
        console.log("Booo");
      });
    }
  
    function error() {
        location_text.textContent = 'Unable to retrieve your location';
    }
  
    if(!navigator.geolocation) {
        location_text.textContent = 'Geolocation is not supported by your browser';
    } else {
        location_text.textContent = 'Locating…';
        navigator.geolocation.getCurrentPosition(success, error);
    }
  
  }
document.getElementById('login_via_facebook').addEventListener('click', (e) => {
    e.preventDefault();
    alert('Logging in via facebook in progress');
})
  
  document.querySelector('#find-me').addEventListener('click', geoFindMe);
  document.querySelector()

  function mapFuelStationServiceToString(str) {
    return "placeholder";
  } 