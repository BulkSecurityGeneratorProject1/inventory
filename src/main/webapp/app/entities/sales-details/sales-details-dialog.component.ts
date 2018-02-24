import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SalesDetails } from './sales-details.model';
import { SalesDetailsPopupService } from './sales-details-popup.service';
import { SalesDetailsService } from './sales-details.service';
import { ProductDetails, ProductDetailsService } from '../product-details';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sales-details-dialog',
    templateUrl: './sales-details-dialog.component.html'
})
export class SalesDetailsDialogComponent implements OnInit {

    salesDetails: SalesDetails;
    isSaving: boolean;

    productdetails: ProductDetails[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private salesDetailsService: SalesDetailsService,
        private productDetailsService: ProductDetailsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productDetailsService.query()
            .subscribe((res: ResponseWrapper) => { this.productdetails = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.salesDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.salesDetailsService.update(this.salesDetails));
        } else {
            this.subscribeToSaveResponse(
                this.salesDetailsService.create(this.salesDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<SalesDetails>) {
        result.subscribe((res: SalesDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SalesDetails) {
        this.eventManager.broadcast({ name: 'salesDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductDetailsById(index: number, item: ProductDetails) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sales-details-popup',
    template: ''
})
export class SalesDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salesDetailsPopupService: SalesDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.salesDetailsPopupService
                    .open(SalesDetailsDialogComponent as Component, params['id']);
            } else {
                this.salesDetailsPopupService
                    .open(SalesDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
