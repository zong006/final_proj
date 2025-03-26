import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../../model/User';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient : HttpClient) { }

  private rootUrl = 'https://finalproj-production-3955.up.railway.app';

  loginUser(user : User) : Observable<User>{
    const loginUrl = this.rootUrl + '/api/user/login'; // <-- set to environment variables
    // const loginUrl =  '/api/user/login'; // <-- set to environment variables
    console.info('>>> url is : ',loginUrl);
    console.info('>>> user: ', user);
    
    const idToken = sessionStorage.getItem('idToken');
    const headers = new HttpHeaders().set(
      'Authorization', `${idToken}`
    );

    return this.httpClient.post<User>(loginUrl, user, {headers : headers}).pipe(
              map(response => response)
            );
    
    
  }

  updateUserPref(user : User){
    const updatePrefUrl = this.rootUrl + `/api/user/update/${user.id}`
    // const updatePrefUrl =  `/api/user/update/${user.id}`
    console.info('>>> url to put: ', updatePrefUrl);
    this.httpClient.put<User>(updatePrefUrl, user).subscribe(
      (response) => {
        console.info('>>> update user pref: ', response);
      }
    );
  }

  // getUserPref(){
  //   this.httpClient.get(this.rootUrl);
  // }
}
