import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LeftoverDetails } from './leftover-details.model';
import { LeftoverDetailsPopupService } from './leftover-details-popup.service';
import { LeftoverDetailsService } from './leftover-details.service';
import { ProductDetails, ProductDetailsService } from '../product-details';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-leftover-details-dialog',
    templateUrl: './leftover-details-dialog.component.html'
})
export class LeftoverDetailsDialogComponent implements OnInit {

    leftoverDetails: LeftoverDetails;
    isSaving: boolean;

    productdetails: ProductDetails[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private leftoverDetailsService: LeftoverDetailsService,
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
        if (this.leftoverDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.leftoverDetailsService.update(this.leftoverDetails));
        } else {
            this.subscribeToSaveResponse(
                this.leftoverDetailsService.create(this.leftoverDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<LeftoverDetails>) {
        result.subscribe((res: LeftoverDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: LeftoverDetails) {
        this.eventManager.broadcast({ name: 'leftoverDetailsListModification', content: 'OK'});
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
    selector: 'jhi-leftover-details-popup',
    template: ''
})
export class LeftoverDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leftoverDetailsPopupService: LeftoverDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.leftoverDetailsPopupService
                    .open(LeftoverDetailsDialogComponent as Component, params['id']);
            } else {
                this.leftoverDetailsPopupService
                    .open(LeftoverDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
