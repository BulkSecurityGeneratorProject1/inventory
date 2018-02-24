import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LeftoverDetails } from './leftover-details.model';
import { LeftoverDetailsService } from './leftover-details.service';

@Component({
    selector: 'jhi-leftover-details-detail',
    templateUrl: './leftover-details-detail.component.html'
})
export class LeftoverDetailsDetailComponent implements OnInit, OnDestroy {

    leftoverDetails: LeftoverDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private leftoverDetailsService: LeftoverDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLeftoverDetails();
    }

    load(id) {
        this.leftoverDetailsService.find(id).subscribe((leftoverDetails) => {
            this.leftoverDetails = leftoverDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLeftoverDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'leftoverDetailsListModification',
            (response) => this.load(this.leftoverDetails.id)
        );
    }
}
