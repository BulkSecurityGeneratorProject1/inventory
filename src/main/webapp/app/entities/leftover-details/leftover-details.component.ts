import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LeftoverDetails } from './leftover-details.model';
import { LeftoverDetailsService } from './leftover-details.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-leftover-details',
    templateUrl: './leftover-details.component.html'
})
export class LeftoverDetailsComponent implements OnInit, OnDestroy {
leftoverDetails: LeftoverDetails[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private leftoverDetailsService: LeftoverDetailsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.leftoverDetailsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.leftoverDetails = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.leftoverDetailsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.leftoverDetails = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInLeftoverDetails();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: LeftoverDetails) {
        return item.id;
    }
    registerChangeInLeftoverDetails() {
        this.eventSubscriber = this.eventManager.subscribe('leftoverDetailsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
