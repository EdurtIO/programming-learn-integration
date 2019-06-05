import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular';

  public list;
  public data = {
    key: '',
    value: ''
  }

  constructor(private http: HttpClient) {
    this.init();
  }

  init() {
    this.http.get('/get').subscribe((res) => {
        this.list = res;
        console.log(res)
      }
    );
  }

  add() {
    const headers = new HttpHeaders().set(
      "Content-type",
      "application/json; charset=UTF-8"
    );
    this.http.post('/post', this.data, {headers}).subscribe();
    this.init();
  }

  delete() {
    this.http.delete('/delete?key=' + this.data.key).subscribe();
    this.init();
  }

  modfiy() {
    const headers = new HttpHeaders().set(
      "Content-type",
      "application/json; charset=UTF-8"
    );
    this.http.post('/post', this.data, {headers}).subscribe();
    this.init();
  }

}
