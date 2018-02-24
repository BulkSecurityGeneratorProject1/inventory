/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { InventoryTestModule } from '../../../test.module';
import { SalesDetailsDialogComponent } from '../../../../../../main/webapp/app/entities/sales-details/sales-details-dialog.component';
import { SalesDetailsService } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.service';
import { SalesDetails } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.model';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details';

describe('Component Tests', () => {

    describe('SalesDetails Management Dialog Component', () => {
        let comp: SalesDetailsDialogComponent;
        let fixture: ComponentFixture<SalesDetailsDialogComponent>;
        let service: SalesDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [SalesDetailsDialogComponent],
                providers: [
                    ProductDetailsService,
                    SalesDetailsService
                ]
            })
            .overrideTemplate(SalesDetailsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDetailsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SalesDetails(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.salesDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salesDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SalesDetails();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.salesDetails = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salesDetailsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
