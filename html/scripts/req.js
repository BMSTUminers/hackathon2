


var create_chain = function(dataset_id, columns)
{

	var default_chain = [{ "name": "loader", "params": {
        "datasourceId": dataset_id,
        "columns": columns
      }
    }];

	var foo =  function( data ) {
		document.chain_id = data.id
		get_preview()
	}

	$.ajax({
		type: "POST",
		headers: { 
			'Accept': 'application/json',
			'Content-Type': 'application/json' 
		},
		url: api_server + "/api/chain/create",
		data: JSON.stringify(default_chain),
		success: foo,
		dataType: 'json'
	});
};

var get_preview = function()
{
	$.getJSON( "/api/chain/" + document.chain_id + "/execute?page=1&pageSize=10", function(data)
	{
		document.current_data = data
	})
};

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

