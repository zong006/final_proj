import { Component, inject, OnInit } from '@angular/core';
import { LoginStore } from '../../store/LoginStore';
import { Observable } from 'rxjs';
import { LeaderboardService } from '../../service/leaderboard/leaderboard.service';
import { User } from '../../model/User';

@Component({
  selector: 'app-leaderboard',
  standalone: false,
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit{

  ngOnInit(): void {
    
    this.loginStore.user$.subscribe(
      (data) => this.user = data
    )

    
    this.doomScore = this.loginStore.doomScrollScore$

    this.leaderboardService.getLeaderBoard().then(
      (data) => {
        let leaderboard = new Map(Object.entries(data));

        this.scores = Array.from(leaderboard.entries())
                            .sort((a, b) => b[1] - a[1]);

        this.scores.forEach(([user, score], index) => {
          // console.info('>>> username: ', this.user.username, user)
          if (user === this.user.username) {
            
            this.doomScore.subscribe(
              (x) => {
                this.scores[index][1] = x
              }
            )
          }
        });
        this.doomScore.subscribe(
          (score) => this.leaderboardService.updateScore(this.user, score)
        )
        
      } 
    )
  }

  private loginStore = inject(LoginStore)
  private leaderboardService = inject(LeaderboardService)

  user : User = {
    id : '',
    displayName : '',
    username : '',
    selectedTopics : []
  }
  doomScore !: Observable<number>
  scores : Array<[string, number]> = []
}
