/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { InventoryTestModule } from '../../../test.module';
import { ProductDetailsDialogComponent } from '../../../../../../main/webapp/app/entities/product-details/product-details-dialog.component';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details/product-details.service';
import { ProductDetails } from '../../../../../../main/webapp/app/entities/product-details/product-details.model';
import { ProductService } from '../../../../../../main/webapp/app/entities/product';

describe('Component Tests', () => {

    describe('ProductDetails Management Dialog Component', () => {
        let comp: ProductDetailsDialogComponent;
        let fixture: ComponentFixture<ProductDetailsDialogComponent>;
        let service: ProductDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [ProductDetailsDialogComponent],
                providers: [
                    ProductService,
                    ProductDetailsService
                ]
            })
            .overrideTemplate(ProductDetailsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProductDetailsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProductDetails(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.productDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'productDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProductDetails();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.productDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'productDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
