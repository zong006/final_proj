import { AfterViewInit, Component, ElementRef, inject, OnDestroy, OnInit } from '@angular/core';
import { LoginStore } from '../../store/LoginStore';
import { User } from '../../model/User';
import { DashboardService } from '../../service/dashboard/dashboard.service';
import { Article } from '../../model/Article';
import { ArticleStore } from '../../store/ArticleStore';
import { LeaderboardService } from '../../service/leaderboard/leaderboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy{
  
  ngAfterViewInit(): void {
    this.attachScrollListener();
  }

  ngOnDestroy(): void {
    this.loginStore.doomScrollScore$.subscribe( 
      (score) => this.leaderboardService.updateScore(this.user, score)
    )
  }

  ngOnInit(): void {

    this.loginStore.user$.subscribe(
      (u) => {
        this.user = u
        console.info('>>> userid: ', u.id)
      }
    )
    this.leaderboardService.getScore(this.user).then(
      (score) => {
        console.info('>>> dashboard doom score: ', score)
        this.loginStore.setDoomScrollScore$(score)
        console.info('>>> dashboard doom score: ', score)
      }
    )
  }

  attachScrollListener() {
    const contentElement = this.elementRef.nativeElement.querySelector('.content');
    if (contentElement) {
      contentElement.addEventListener('scroll', this.onScroll.bind(this));
    }
  }

  private loginStore = inject(LoginStore);
  private articleStore = inject(ArticleStore);
  private dashboardSvc = inject(DashboardService);
  private leaderboardService = inject(LeaderboardService);

  private elementRef = inject (ElementRef)

  articles$ = this.articleStore.articles$;

  isLoading$ = this.articleStore.isLoading$;
  currPage$ = this.articleStore.currPage$;

  user : User = {
    id : '',
    username : '',
    displayName : '',
    selectedTopics : []
  }

  loggedIn = this.loginStore.loggedIn$; // <---- for route guard checking

  currPage = 1;

  getFeed(user : User){
    this.articleStore.setLoadingState(true);
    this.currPage$.subscribe(x => this.currPage = x)
    
    this.dashboardSvc.getFeed(user, this.currPage).subscribe(
      (data : Article[]) => {
        this.articleStore.updateArticles(data);
        this.articleStore.incrCurrPage(1);
        this.articleStore.setLoadingState(false);
      }
    )
  }
  
  openArticle(url: string): void { // <-- open article in new window upon click
    window.open(url, '_blank'); 
  }

  onScroll(){
    // console.info('>>> scrolled to page: ', this.currPage);
    this.loginStore.incrDoomScrollStoreDelta$(10);
   
    
    this.getFeed(this.user);
  }



}
