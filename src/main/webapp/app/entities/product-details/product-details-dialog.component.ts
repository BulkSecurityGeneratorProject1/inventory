import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProductDetails } from './product-details.model';
import { ProductDetailsPopupService } from './product-details-popup.service';
import { ProductDetailsService } from './product-details.service';
import { Product, ProductService } from '../product';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-product-details-dialog',
    templateUrl: './product-details-dialog.component.html'
})
export class ProductDetailsDialogComponent implements OnInit {

    productDetails: ProductDetails;
    isSaving: boolean;

    products: Product[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private productDetailsService: ProductDetailsService,
        private productService: ProductService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.productDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productDetailsService.update(this.productDetails));
        } else {
            this.subscribeToSaveResponse(
                this.productDetailsService.create(this.productDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProductDetails>) {
        result.subscribe((res: ProductDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProductDetails) {
        this.eventManager.broadcast({ name: 'productDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductById(index: number, item: Product) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-product-details-popup',
    template: ''
})
export class ProductDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productDetailsPopupService: ProductDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.productDetailsPopupService
                    .open(ProductDetailsDialogComponent as Component, params['id']);
            } else {
                this.productDetailsPopupService
                    .open(ProductDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
