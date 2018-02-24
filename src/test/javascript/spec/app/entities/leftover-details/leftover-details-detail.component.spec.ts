/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { InventoryTestModule } from '../../../test.module';
import { LeftoverDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details-detail.component';
import { LeftoverDetailsService } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.service';
import { LeftoverDetails } from '../../../../../../main/webapp/app/entities/leftover-details/leftover-details.model';

describe('Component Tests', () => {

    describe('LeftoverDetails Management Detail Component', () => {
        let comp: LeftoverDetailsDetailComponent;
        let fixture: ComponentFixture<LeftoverDetailsDetailComponent>;
        let service: LeftoverDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [LeftoverDetailsDetailComponent],
                providers: [
                    LeftoverDetailsService
                ]
            })
            .overrideTemplate(LeftoverDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeftoverDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeftoverDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new LeftoverDetails(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.leftoverDetails).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
