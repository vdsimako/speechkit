const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const axios = require('axios');


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {status: ""};
    }

    componentDidMount() {
        client({method: 'GET', path: '/actuator/health'}).done(response => {
            this.setState({status: response.entity.status})
        });
    }

    render() {
        return (
            <div>
                <Status status={this.state.status}/>
                <NameForm/>
                <UploadFileForm/>
            </div>
        )
    }
}

class Status extends React.Component {
    render() {
        return (
            <p>Status is {this.props.status}</p>
        )
    }
}


class NameForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch("/translate", {
            method: 'POST', // или 'PUT'
            body: JSON.stringify({"text": this.state.value}), // данные могут быть 'строкой' или {объектом}!
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            response.blob().then(blob => {
                let url = window.URL.createObjectURL(blob);
                let a = document.createElement('a');
                a.href = url;
                a.download = 'test_file';
                a.click();
            });
            //window.location.href = response.url;
        });

    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Put text to translate here:
                    <input type="text" value={this.state.value} onChange={this.handleChange}/>
                </label>
                <input type="submit" value="Submit"/>
            </form>
        );
    }
}

class UploadFileForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};
    }

    // handleChange(event) {
    //     this.setState({value: event.target.value});
    // }

    handleUpload(event) {
        // event.preventDefault();
        event.persist()
        var file = event.target.files[0];
        const formData = new FormData();
        formData.append('file',  file, file.name);

        let target = event.currentTarget;

        axios
            .post("/translate", formData)
            .then((res) => {
                // alert("File Upload success");

                this.setState({value: res.data.result})
            })
            .catch((err) => {
                console.log(err)
                // alert("File Upload Error")
            });

    }

    render() {
        return (
            <div>
                <input type="file" onChange={(event) => {event.persist();this.handleUpload(event)}} />
                <p>Text in audio file is: {this.state.value}</p>
            </div>
        );
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)