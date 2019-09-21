// Call the dataTables jQuery plugin
// $(document).ready(function() {
//   $('#dataTable').DataTable();
//   "scrollX": true;
// });

// $(document).ready(function() {
//     $('#dataTable').DataTable( {
//         "scrollY": true
//     } );
// } );
$(document).ready(function () {
    $('#dataTable').DataTable({
        "scrollY": 250,
        "scrollX": true
    });
});