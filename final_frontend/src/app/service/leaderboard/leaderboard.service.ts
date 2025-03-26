import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { User } from '../../model/User';

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {

  private http = inject(HttpClient);

  private url = 'https://finalproj-production-3955.up.railway.app'

  getLeaderBoard(){
    return lastValueFrom(this.http.get<Map<string, number>>(this.url + '/api/leaderboard/get'))
  }

  getScore(user: User){
    return lastValueFrom(this.http.get<number>(this.url + `/api/leaderboard/${user.username}`))
  }

  updateScore(user : User, score : number){
    return this.http.post<string>( this.url + `/api/leaderboard/update/${user.username}`, score).subscribe(
      (response) => console.info('>>> update scroe response: ', response)
    )
  }
}
