import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WasteDetails } from './waste-details.model';
import { WasteDetailsService } from './waste-details.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-waste-details',
    templateUrl: './waste-details.component.html'
})
export class WasteDetailsComponent implements OnInit, OnDestroy {
wasteDetails: WasteDetails[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private wasteDetailsService: WasteDetailsService,
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
            this.wasteDetailsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.wasteDetails = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.wasteDetailsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.wasteDetails = res.json;
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
        this.registerChangeInWasteDetails();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WasteDetails) {
        return item.id;
    }
    registerChangeInWasteDetails() {
        this.eventSubscriber = this.eventManager.subscribe('wasteDetailsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
