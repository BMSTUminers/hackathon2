


var create_chain = function(dataset_id, columns)
{
	$.ajaxSetup({
		async: false
	});

	document.api_server = "http://127.0.0.1:8081"
	var default_chain = [{ "name": "loader", "params": {
        "datasourceId": dataset_id,
        "columns": columns
      }
    }];

	var foo =  function( data ) {
		document.chain_id = data.id;
		get_full();
	}

	$.ajax({
		type: "POST",
		headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
		},
		url: document.api_server + "/api/chain/create",
		data: JSON.stringify(default_chain),
		success: foo,
		dataType: 'json'
	});
};

var get_preview = function(callback)
{
	$.getJSON( api_server + "/api/chain/" + document.chain_id + "/execute?page=1&pageSize=10", function(data)
	{
		document.current_data = data;
		callback()
	})
};

var get_full = function(callback)
{
	document.api_server = "http://127.0.0.1:8081";
	$.getJSON( document.api_server + "/api/chain/" + document.chain_id + "/execute", function(data)
	{
		document.current_data = data;
		document.dataTransferred = true;
		//console.log(data);

	})
}


var set_filters = function(dataset_id, columns, filters)
{
	var data = [{ "name": "loader", "params": {
        "datasourceId": dataset_id,
        "columns": columns
      }
    }, filters];

    var foo =  function( data ) {
		document.chain_id = data.id;
		get_preview();
	};

	$.ajax({
		type: "POST",
		headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
		},
		url: api_server + "/api/chain/" + document.chain_id + "/update",
		data: JSON.stringify(default_chain),
		success: foo,
		dataType: 'json'
	});
};

var remove_chain = function()
{
	$.get( api_server + "/api/chain/" + document.chain_id + "/delete")
};

