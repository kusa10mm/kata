package kata.ex01;

import kata.ex01.model.HighwayDrive;
import kata.ex01.model.RouteType;
import kata.ex01.model.VehicleFamily;
import kata.ex01.util.HolidayUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author kawasima
 */
public class DiscountServiceImpl implements DiscountService {
    @Override
    public long calc(HighwayDrive drive) {
        boolean isWeekAndMorningDisc = isWeekAndMorningDisc(drive);
        int monthCount = drive.getDriver().getCountPerMonth();
        if(isWeekAndMorningDisc && monthCount >= 10) {
            return 50;
        } else if(isWeekAndMorningDisc && (monthCount >=5)) return 30;


        boolean isHolidayDisc = isHolidayDisc(drive);
        if (isHolidayDisc) {
            return 30;
        }

        // boolean isMidnightDisc = isMidnightDisc();
        // if (isMidnightDisc) {
        //     return 30;
        // }

        return 0;
    }

    private boolean isWeekAndMorningDisc(HighwayDrive drive) {
        boolean isMorningOrEvening = checkIsMorningOrEvening(drive.getEnteredAt(), drive.getExitedAt());
        boolean isWeekday = needWeekdayDiscCheck(drive.getEnteredAt(), drive.getExitedAt());
        return isMorningOrEvening && drive.getRouteType() == RouteType.RURAL && isWeekday;
    }

    private boolean isHolidayDisc(HighwayDrive drive) {
        boolean isHoliday = needHolidayDiscCheck(drive.getEnteredAt(), drive.getExitedAt());
        return isHoliday && drive.getRouteType() == RouteType.RURAL && (drive.getVehicleFamily() == VehicleFamily.STANDARD || drive.getVehicleFamily() ==  VehicleFamily.MINI);
    }

    private boolean isMidnight(int enteredHour, int exitHour) {
        int RuleStart = 0;
        int RuleEnd = 4;
        return enteredHour <= RuleEnd && exitHour >= RuleStart;
    }

    private boolean checkIsMorningOrEvening(LocalDateTime enteredAt, LocalDateTime exitAt) {
        LocalDateTime MorningStart = LocalDateTime.of(exitAt.toLocalDate(), LocalTime.of(6,0));
        LocalDateTime MorningEnd = LocalDateTime.of(exitAt.toLocalDate(), LocalTime.of(9,0));
        LocalDateTime EveningStart = LocalDateTime.of(enteredAt.toLocalDate(), LocalTime.of(17,0));
        LocalDateTime EveningEnd = LocalDateTime.of(enteredAt.toLocalDate(), LocalTime.of(20,0));

        if (enteredAt.isBefore(MorningEnd) && exitAt.isAfter(MorningStart)) {
            return true;
        } else return enteredAt.isBefore(EveningEnd) && exitAt.isAfter(EveningStart);
    }


    private boolean needWeekdayDiscCheck(LocalDateTime enteredAt, LocalDateTime exitAt) {
        //どちらかが平日なら平日として、判定。休日判定の処理には使えない
        return !HolidayUtils.isHoliday(enteredAt.toLocalDate()) || !HolidayUtils.isHoliday(exitAt.toLocalDate());
    }

    private boolean needHolidayDiscCheck(LocalDateTime enteredAt, LocalDateTime exitAt) {
        //どちらか休日なら休日として判定
        return HolidayUtils.isHoliday(enteredAt.toLocalDate()) || HolidayUtils.isHoliday(exitAt.toLocalDate());
    }
}
