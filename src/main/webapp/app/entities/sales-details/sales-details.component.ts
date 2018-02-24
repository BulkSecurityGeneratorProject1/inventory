import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SalesDetails } from './sales-details.model';
import { SalesDetailsService } from './sales-details.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sales-details',
    templateUrl: './sales-details.component.html'
})
export class SalesDetailsComponent implements OnInit, OnDestroy {
salesDetails: SalesDetails[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private salesDetailsService: SalesDetailsService,
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
            this.salesDetailsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.salesDetails = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.salesDetailsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.salesDetails = res.json;
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
        this.registerChangeInSalesDetails();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SalesDetails) {
        return item.id;
    }
    registerChangeInSalesDetails() {
        this.eventSubscriber = this.eventManager.subscribe('salesDetailsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
