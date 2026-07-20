
// 
// console.log(tableBody);

async function loadServices(){
    const response = await fetch("/api/services");
    return await response.json();
}

async function mainVue(){
    const body = document.getElementById("vue");
    body.innerHTML = "";   //  flush old data
    const services = await loadServices();

    

    for(const service of services){

        const card = document.createElement("div");
        card.addEventListener("click", () => {
            showHistory(service);
        });
        
        card.className = "service-card";

        card.innerHTML = `
                    <h3>${service.name}</h3>
                    <b>Description : </b> <strong>${service.description}</strong> <br>
                    <b>Status : </b> <strong>${service.status}</strong> <br>
                    <b>Response Time : </b> <strong>${service.responseTime}</strong> <br>
        `;
        body.appendChild(card);
    }

}

async function showHistory(service){

    const historyBody = document.getElementById("historyPane");
    const title = document.getElementById("titleOfHistoryPane");
    const content = document.getElementById("historyContent");

    title.innerHTML = "";   //  flush old data
    content.innerHTML = "";   //  flush old data
    //historyBody.innerHTML = "";   //  flush old data

    const historyQueue = service.historyQueue;
    title.textContent = service.name;

    for(const history of historyQueue){

        const tempPane = document.createElement("div");
        tempPane.innerHTML = `
                    <br><br>
                    <b>Status : </b> <strong>${history.serviceStatus}</strong> <br>
                    <b>HTTP Status Code : </b> <strong>${history.httpStatus}</strong> <br>
                    <b>Response Time : </b> <strong>${history.responseTime}</strong> <br>
                    <b>Last Check : </b> <strong>${history.timeStamp}</strong> <br>
                    <br><br>
        `;
        content.appendChild(tempPane);
    }

    historyBody.style.display = "block";
}

async function checkLatestReports(){
    
    const tableBody = document.getElementById("latestReportBody");
    tableBody.innerHTML = "";   //  flush old data
    let services = await loadServices();


    for(let service of services){
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        const statusCell = document.createElement("td");
        const httpCell = document.createElement("td");
        const responseTimeCell = document.createElement("td");

        nameCell.textContent = service.name;
        statusCell.textContent = service.status;
        httpCell.textContent = service.httpStatusCode;
        responseTimeCell.textContent = service.responseTime;

        row.appendChild(nameCell);
        row.appendChild(statusCell);
        row.appendChild(httpCell);
        row.appendChild(responseTimeCell);

        tableBody.appendChild(row);
    }
}

async function loadStatistics(){
    const globalStatusArea = document.getElementById("globalStatusData");
    const mainStatsArea = document.getElementById("stats");
    
    mainStatsArea.innerHTML = "";   //  flush old data
    globalStatusArea.innerHTML = "";   //  flush old data
    
    const tempRow = document.createElement("tr");

    const response = await fetch("/api/services");
    const result = await response.json();

    let counter = 0;
    let averageResponseTime = 0;

    let upCounter = 0;
    let degradedCounter = 0;
    let downCounter = 0;
    let timeoutCounter = 0;

    for(const service of result){
        counter++;
        averageResponseTime = averageResponseTime + service.responseTime;

        switch(service.status){
            case "UP":
                upCounter++;
                break;
            case "DEGRADED":
                degradedCounter++;
                break;
            case "DOWN":
                downCounter++;
                break;
            case "TIMEOUT":
                timeoutCounter++;
                break;
            default :
                console.log("Error, this Service status does not match a real status --> " + service.status);
        }
    }

    averageResponseTime = averageResponseTime / counter ;   
    const nbrOfServices = document.createElement("h3");
    const responseTimeOnAverage = document.createElement("h3");

    nbrOfServices.textContent = "Number Of Services : " + counter;
    responseTimeOnAverage.textContent = "Average Response Time : " + averageResponseTime;

    const upCell = document.createElement("td");
    const degradedCell = document.createElement("td");
    const downCell = document.createElement("td");
    const timeoutCell = document.createElement("td");

    upCell.textContent = upCounter;
    degradedCell.textContent = degradedCounter;
    downCell.textContent = downCounter;
    timeoutCell.textContent = timeoutCounter;

    mainStatsArea.appendChild(nbrOfServices);
    mainStatsArea.appendChild(responseTimeOnAverage);

    globalStatusArea.appendChild(upCell);
    globalStatusArea.appendChild(degradedCell);
    globalStatusArea.appendChild(downCell);
    globalStatusArea.appendChild(timeoutCell);

    //globalStatusArea.appendChild(tempRow);

}


async function requestServiceDetailsBySearch(){
    const searchBox = document.getElementById("searchBoxServices");

    searchBox.addEventListener("input",async () => {
        const user_input = searchBox.value.trim();

        if(user_input === "")
            return;

        const response = await fetch("/api/services/" + user_input);
        if(!response.ok)
            return;

        const result = await response.json();

        const displayArea = document.getElementById("displayAreaForSearchBox");
        displayArea.innerHTML = `
            <h3>${result.name}</h3>
            <p>Status : ${result.status}</p>
            <p>HTTP : ${result.httpStatusCode}</p>
            <p>Response : ${result.responseTime} ms</p>
        `;
    });
}

async function main(){
    mainVue();
    checkLatestReports();
    loadStatistics();
    requestServiceDetailsBySearch();

    document.getElementById("closePane").addEventListener("click", () =>{
        document.getElementById("historyPane").style.display = "none";
    });
    
    setInterval(async() => {
        await mainVue();
        await checkLatestReports();
        await loadStatistics();}
    ,5_000);     //  5 seconds
}

main();