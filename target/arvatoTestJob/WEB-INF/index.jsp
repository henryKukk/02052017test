<head>


    <script
            src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>
    <script
            src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"
            integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="css/styles.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <script src="js/scripts.js"/>
</head>
<html>
<script>

</script>

<body>
<h1 class="main_heading">Invoice handler</h1>
<div id="button_container">
<button type="button" class="btn btn-info btn-lg" onclick="openModal()">Add Parking info</button>
<button type="button" class="btn btn-info btn-lg" onclick="openInfoModal()">Show info of users</button>
<button type="button" class="btn btn-info btn-lg" onclick="getInvoice()">GenerateInvoice</button>

</div>



<!-- MODALS START -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add</h4>
            </div>
            <div class="modal-body">
                <label for="selectUser">Select user</label>
                <select class="form-control" id="selectUser"></select>
                <label for="selectParkHouse">Select park house</label>
                <select class="form-control" id="selectParkHouse">
                    <option value="0">Dummy parkhouse</option>
                </select>
                <br/>
                <label for="start_date">Enter start date</label>
                <input class="form-control" type="date" name="startDate" id="start_date"/>
                <label for="start_time" class="inline">Start time</label>
                <input class="form-control" type="text" name="startTime" id="start_time" placeholder="HH:MM"/><br/>
                <label for="end_date">Enter end date</label>
                <input class="form-control" type="date" name="endDate" id="end_date" />
                <label for="start_time" class="inline">End time</label>
                <input class="form-control" type="text" name="endTime" id="end_time" placeholder="HH:MM"/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-success" onclick="addParking()">Add</button>
            </div>
        </div>
    </div>
</div>

<div id="parkingInfo" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">See info of customer</h4>
            </div>
            <div class="modal-body">
                <select class="form-control" id="info_user" onchange="getNewInfo()">
                </select>
                <table class="table table-striped" id="info_table">
                    <thead>
                <tr>
                    <th>Start time</th>
                    <th>End Time</th>
                    <th>Total Price</th>
                    <th>Park house ID</th>
                </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div id="invoice" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content invoice">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Invoice for customer</h4>
            </div>
            <div class="modal-body" id="invoice_data">
                <select class="form-control" id="invoice_customer" onchange="getNewInvoiceData()"></select>
                <label for="datePicker">Write month</label>
                <input class="form-control" type="text" class="month_year" id="datePicker" placeholder="MM/YYYY">
                <button type="button" class="btn btn-success get_invoice" onclick="getNewInvoiceData()">Get invoice</button>
                <div id="invoice_wrapper"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
    </div>

<div class="invoice_area" id="invoice_area_clean" style="display:none">
    <div id="invoice_heading" class="invoice_heading_class"><h3>Invoice</h3></div>
    <div id="name"><span class="title">Customer name:</span><span class="content" id="name_content">Name</span></div>
    <div id="parkings">
        <table class="table table-striped" id="parkings_table">
            <thead>
            <tr>
                <th>Start</th>
                <th>End</th>
                <th>Amount</th>
                <th>Park house ID</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
    <div id="monthly_fee"><span class="title">Monthly fee:</span><span class="content" id="monthly_content">Monthly fee</span></div>
    <div id="total_fee"><span class="title">Total parking:</span><span class="content" id="total_content">Total fee</span></div>
    <div id="total_all"><span class="title">Total to pay:</span><span class="content" id="total_all_content">Total fee</span></div>
</div>
</body>
</html>
