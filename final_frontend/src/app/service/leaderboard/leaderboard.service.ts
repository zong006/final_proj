import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { User } from '../../model/User';

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {

  private http = inject(HttpClient);

  // private url = 'http://localhost:8080'

  getLeaderBoard(){
    return lastValueFrom(this.http.get<Map<string, number>>('/api/leaderboard/get'))
  }

  getScore(user: User){
    return lastValueFrom(this.http.get<number>(`/api/leaderboard/${user.username}`))
  }

  updateScore(user : User, score : number){
    return this.http.post<string>( `/api/leaderboard/update/${user.username}`, score).subscribe(
      (response) => console.info('>>> update scroe response: ', response)
    )
  }
}
