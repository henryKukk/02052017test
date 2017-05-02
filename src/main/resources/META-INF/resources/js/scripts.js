function addItem(item, object) {
    object.append(
        "<option value="+ item.customerID + ">" +
        item.name +
        "</option>");
}
function addParking() {
    var startTime = $("#start_time").val().split(":");
    var endTime = $("#end_time").val().split(":");
    if (startTime.length != 2) {
        console.log("Not working Start")
        return;
    }
    if (endTime.length != 2) {
        console.log("Not working End");
        return;
    }
    var startHour = startTime[0];
    var endHour = endTime[0];
    var startMinute = startTime[1];
    var endMinute = endTime[1];
    var endDate = new Date($("#end_date").val());
    endDate.setHours(endHour);
    endDate.setMinutes(endMinute);
    var startDate = new Date($("#start_date").val());
    startDate.setHours(startHour);
    startDate.setMinutes(startMinute);
    var json = makeJson(startDate, endDate);
    $.ajax({
        url: "rest/add_parking",
        method: "POST",
        data: JSON.stringify(json),
        contentType: "application/json",
        success: function(data) {
            console.log(data);
            $("#myModal").modal("hide");
        }
    })
}
function makeJson(startDate, endDate) {
    var customerID = $("#selectUser").val();
    var parkHouseID = $("#selectParkHouse").val();
    var json = {
        customerID: parseInt(customerID),
        parkHouseID: parseInt(parkHouseID),
        startTime: startDate.getTime(),
        endTime: endDate.getTime()
    }
    return json;
}
function openModal() {
    $("#myModal").modal("show");
    $("#selectUser").empty();
    $("input").val("");
    $.get("rest/get_all_customers", function(data) {
        $.each(data, function(i, item) {
            addItem(item, $("#selectUser"));
        })
    })
}
function openInfoModal() {
    $("#parkingInfo").modal("show");
    $("#info_user").empty();
    $.get("rest/get_all_customers", function(data) {
        $.each(data, function(i, item) {
            addItem(item, $("#info_user"));
        })
        getNewInfo();
    })
}
function getNewInfo() {
    var userId = $("#info_user").val();
    $.get("rest/get_user_parkings", {userID: userId}, function(data) {
        addDataToTable(data);
    })
}
function addDataToTable(data) {
    $("#info_table").find("tbody").empty();
    $.each(data, function(i, item) {
        addRowToTable(item);
    })
}
function addRowToTable(item) {
    var startTime = getTimeString(item.startTime);
    var endTime = getTimeString(item.endTime);
    console.log(item);
    $("#info_table").append("<tr>" +
        "<td>" + startTime +
        "</td><td>" + endTime +
        "</td><td>" + item.paymentAmount +
        " EUR</td><td>" + item.parkHouseId +
        "</td></tr>")
}
function getTimeString(time) {
    return formatNumber(time.monthValue) + "/" + formatNumber(time.dayOfMonth) + "/" + time.year + " " + formatNumber(time.hour) + ":" + formatNumber(time.minute);
}
function formatNumber(number) {
    var formattedNumber = ("0" + number).slice(-2);
    return formattedNumber;

}
function getInvoice() {
    $("#invoice").modal("show");
    $("#invoice_customer").empty();
    $.get("rest/get_all_customers", function(data) {
        $.each(data, function(i, item) {
            addItem(item, $("#invoice_customer"));
        })
    })
}


function getNewInvoiceData() {
    var monthYear = $('#datePicker').val().split("/");
    if (monthYear.length != 2) {
        return;
    }
    var date = new Date(monthYear[1], monthYear[0] - 1, 1, 0, 0);
    var customer = $("#invoice_customer").val();
    $.get("rest/get_invoice", {userID: customer, timestamp: date.getTime()}, function(data) {
        $("#invoice_wrapper").empty();
        setInvoices(data);
    })
}
function setInvoices(data) {
    $.each(data, function(i, item) {
        var copy = $("#invoice_area_clean").clone();
        copy.attr("id", "invoice_area_" + i);
        copy.find("#name_content").text(item.customer.name);
        copy.find("#total_content").text(item.amount + " EUR");
        copy.find("#monthly_content").text(item.monthlyFee + " EUR");
        copy.find("#total_all_content").text(item.monthlyFee + item.amount + " EUR");
        addAllRows(copy, item.parkings);
        copy.removeAttr("style");
        $("#invoice_wrapper").append(copy);
    });
}
function addAllRows(copy, parkings) {
    $.each(parkings, function(i, parking) {
        var table = copy.find("#parkings_table");
        var startTime = getTimeString(parking.startTime);
        var endTime = getTimeString(parking.endTime);
        var rowToAdd = "<tr><td>" + startTime + "</td><td>" + endTime + "</td><td>" + parking.paymentAmount + " EUR</td><td>" + parking.parkHouseId + "</td></tr>";
        table.append(rowToAdd);
    })
}