import axios from 'axios';

export default {
	downloadFile(owner, startDate, endDate, params) {
		axios({
			// url: 'http://localhost:8080/trader/statement/Bob',
			url: (process.env.STATEMENT_HOST || "https://172.17.76.32:31010") + '/trader/statement/'+ owner,
			// url: '/trader/statement/'+owner,
			method: 'GET',
      responseType: 'blob',
      params: params
		}).then((response) => {
			if (window.confirm('Are you sure you want to download file?')) {
				var fileURL = window.URL.createObjectURL(new Blob([response.data]));
				var fileLink = document.createElement('a');

				fileLink.href = fileURL;
				fileLink.setAttribute('download', 'statement-'+ startDate +'-'+ endDate +'.pdf');
				document.body.appendChild(fileLink);
				fileLink.click();
			}
		});
	}
}
