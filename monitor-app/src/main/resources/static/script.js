
// 
// console.log(tableBody);

async function loadServices(){
    const response = await fetch("/api/services");
    return await response.json();
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
    globalStatusArea.innerHTML = "";   //  flush old data
    const tempRow = document.createElement("tr");

    const response = await fetch("/api/stats");
    const result = await response.json();

    const upCell = document.createElement("td");
    const degradedCell = document.createElement("td");
    const downCell = document.createElement("td");
    const timeoutCell = document.createElement("td");

    upCell.textContent = result.UP;
    degradedCell.textContent = result.DEGRADED;
    downCell.textContent = result.DOWN;
    timeoutCell.textContent = result.TIMEOUT;

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

    checkLatestReports();
    loadStatistics();
    requestServiceDetailsBySearch();
    
    setInterval(async() => {
        await checkLatestReports();
        await loadStatistics();}
    ,5000);
}

main();