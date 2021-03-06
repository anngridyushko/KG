# Лабораторная работа 1
**Компьютерная графика**

**Описание:** Десктопное приложение, позволяющее пользователю выбирать, а затем интерактивно менять цвет, показывая при этом его составляющие в трех
моделях одновременно: RGB, CMYK, HLS. Приложение написано на языке Kotlin при помощи Jetpack Compose for Desktop.

**Установка**: Приложение требует установки при помощи файла с расширением .exe. Прежде чем запустить приложение, нужно дважды кликнуть на установщик KG_lab1-1.0.exe и следовать предложенным инструкциям. После успешного завершения установки, для запуска приложения достаточно в папке с приложением найти файл KG_lab1.exe.

**Использование**: Пользователю дана возможность выбирать цета тремя способами: 
1) Задать точные цвета с помощью полей ввода;
2) Выбрать цвета из предложенной палитры;
3) Задать цвета, перетаскивая ползунки для каждого параметра всех цветовых моделей;

При изменении любой компоненты цвета все остальные представления этого цвета в двух других цветовых моделях пересчитываются автоматически. 

В приложении реализовано предупреждение о некорректном пересчете, однако в данных моделях оно не понадобилось.

Границы диапазонов допустимых значений при вводе параметров с помощью полей ввода:

RGB: R[0..255], G[0..255], B[0..255]

CMYK: каждый из цветов описывается градацией от 0 до 1

HLS: H от 0 до 360, L и S от 0 до 1

При попытке изменить CMYK остальные компоненты могут изменяться в зависимости от значения K. Реализация в приложении не позволяет добиться темного цвета только компонентами C, M и Y.
