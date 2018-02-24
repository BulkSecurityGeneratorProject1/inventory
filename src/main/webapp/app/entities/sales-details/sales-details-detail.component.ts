import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SalesDetails } from './sales-details.model';
import { SalesDetailsService } from './sales-details.service';

@Component({
    selector: 'jhi-sales-details-detail',
    templateUrl: './sales-details-detail.component.html'
})
export class SalesDetailsDetailComponent implements OnInit, OnDestroy {

    salesDetails: SalesDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private salesDetailsService: SalesDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSalesDetails();
    }

    load(id) {
        this.salesDetailsService.find(id).subscribe((salesDetails) => {
            this.salesDetails = salesDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSalesDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'salesDetailsListModification',
            (response) => this.load(this.salesDetails.id)
        );
    }
}
