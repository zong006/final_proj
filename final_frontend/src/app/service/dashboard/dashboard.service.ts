import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../model/User';
import { Article } from '../../model/Article';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private rootUrl = 'https://finalproj-production-3955.up.railway.app';

  constructor(private httpClient : HttpClient){ }

  getFeed(user : User, page : number){
    
    let idToken = sessionStorage.getItem('idToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer${idToken}`);

    const feedUrl = this.rootUrl + `/api/articles/feed/${user.id}?page=${page}`
    // const feedUrl = `/api/articles/feed/${user.id}?page=${page}`
    console.info('>>> feedUrl: ', feedUrl)

    return this.httpClient.get<Article[]>(feedUrl, {headers : headers});
  }

  getQrCode(user : User){
    // const qrUrl = this.rootUrl + `/qr/generate/${user.id}`
    const qrUrl = this.rootUrl +  `/api/qr/generate/${user.id}`

    console.info('>>> QR code Url: ', qrUrl)
    return this.httpClient.get(qrUrl, { responseType: 'blob' })
  }
}
