// eslint-disable-next-line import/no-extraneous-dependencies
const { Builder, By, until } = require('selenium-webdriver');
// eslint-disable-next-line import/no-extraneous-dependencies
const chrome = require('selenium-webdriver/chrome');

// eslint-disable-next-line no-undef
jest.setTimeout(30000); // increase timeout for slow browsers

// eslint-disable-next-line no-undef
describe('SeatAlertPage UI (Selenium)', () => {
  let driver;

  // eslint-disable-next-line no-undef
  beforeAll(async () => {
    const options = new chrome.Options();
    options.addArguments('--headless');
    options.addArguments('--no-sandbox');
    options.addArguments('--disable-dev-shm-usage');

    driver = await new Builder().forBrowser('chrome').setChromeOptions(options).build();
    await driver.get('http://localhost:3000/seat-alert'); // Ensure frontend is running
  });

  // eslint-disable-next-line no-undef
  afterAll(async () => {
    await driver.quit();
  });

  // eslint-disable-next-line no-undef
  test('fills out the form and submits subscription', async () => {
    const courseInput = await driver.findElement(By.id('courseCodeInput'));
    await courseInput.sendKeys('COMPSCI 1MD3');

    const emailInput = await driver.findElement(By.id('emailInput'));
    await emailInput.sendKeys('test@example.com');

    const termSelect = await driver.findElement(By.id('termSelect'));
    await termSelect.sendKeys('Winter-2025');

    const button = await driver.findElement(By.css('button.seat-alert-button'));
    await button.click();

    const successBox = await driver.wait(
      until.elementLocated(By.css('.success-box')),
      10000
    );

    const successText = await successBox.getText();
    // eslint-disable-next-line no-undef
    expect(successText).toMatch(/Subscribed successfully/i);
  });
});
