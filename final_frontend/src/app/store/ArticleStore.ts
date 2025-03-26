import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Article } from "../model/Article";

export interface ArticleState{
    isLoading : boolean
    currPage : number
    articles : Article[]
}

@Injectable({ providedIn : 'root' })

export class ArticleStore extends ComponentStore<ArticleState>{
    constructor(){
        super(
            {
                isLoading : false,
                currPage : 1,
                articles : []
            }
        )
    }

    readonly articles$ = this.select(currState => currState.articles);
    readonly isLoading$ = this.select(currState => currState.isLoading);
    readonly currPage$ = this.select(currState => currState.currPage);

    readonly updateArticles = this.updater<Article[]>(
        (currState : ArticleState, newArticles : Article[]) => {
            const newState : ArticleState = {
                currPage : currState.currPage,
                isLoading : currState.isLoading,
                articles : [...currState.articles, ...newArticles]
            }
            return newState;
        }
    )

    readonly setLoadingState = this.updater<boolean>(
        (currState : ArticleState, newLoadingState : boolean) => {
            const newState : ArticleState = {
                currPage : currState.currPage,
                articles : currState.articles,
                isLoading : newLoadingState
            }
            return newState;
        }
    )

    readonly resetCurrPage$ = this.updater<number>(
        (currState : ArticleState , zero : number) => {
            const newState : ArticleState = {
                isLoading : currState.isLoading,
                articles : currState.articles,
                currPage : zero
            }
            return newState;
        }
    )

    readonly incrCurrPage = this.updater<number>(
        (currState : ArticleState , incrPage : number) => {
            const newState : ArticleState = {
                isLoading : currState.isLoading,
                articles : currState.articles,
                currPage : currState.currPage + incrPage
            }
            return newState;
        }
    )

    readonly clear = this.updater((state) => {
        return {
            ...state,
        articles: [],
        currPage: 1,
        isLoading: false,
        }  
      });
}