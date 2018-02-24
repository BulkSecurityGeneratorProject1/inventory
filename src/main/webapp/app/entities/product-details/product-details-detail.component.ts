import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ProductDetails } from './product-details.model';
import { ProductDetailsService } from './product-details.service';

@Component({
    selector: 'jhi-product-details-detail',
    templateUrl: './product-details-detail.component.html'
})
export class ProductDetailsDetailComponent implements OnInit, OnDestroy {

    productDetails: ProductDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private productDetailsService: ProductDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProductDetails();
    }

    load(id) {
        this.productDetailsService.find(id).subscribe((productDetails) => {
            this.productDetails = productDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProductDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'productDetailsListModification',
            (response) => this.load(this.productDetails.id)
        );
    }
}
