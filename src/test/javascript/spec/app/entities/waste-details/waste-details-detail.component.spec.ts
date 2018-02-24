/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { InventoryTestModule } from '../../../test.module';
import { WasteDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/waste-details/waste-details-detail.component';
import { WasteDetailsService } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.service';
import { WasteDetails } from '../../../../../../main/webapp/app/entities/waste-details/waste-details.model';

describe('Component Tests', () => {

    describe('WasteDetails Management Detail Component', () => {
        let comp: WasteDetailsDetailComponent;
        let fixture: ComponentFixture<WasteDetailsDetailComponent>;
        let service: WasteDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [WasteDetailsDetailComponent],
                providers: [
                    WasteDetailsService
                ]
            })
            .overrideTemplate(WasteDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WasteDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WasteDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new WasteDetails(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.wasteDetails).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
